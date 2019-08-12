package com.example.indigo24;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indigo24.adapterForList.adapter.SliderForPaysAdapter;
import com.example.indigo24.adapterForList.objected.SliderForPays;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class pays_keys extends Fragment implements SliderForPaysAdapter.OnNoteListener {
    View view;
    Button addBalance, vivod, open_all_servicess, click_open_pays2, tranzactionButton;
    ImageButton barcode, btnValutesTG, btnValutesDL, btnValutesRB, btnValutesEUR;
    TextView balance, blockBalance, courseDollar, courseRuble, courseEuro, userName;
    ImageView balanceBlock, balanceDostup;
    FragmentTransaction fTrans;
    private final OkHttpClient client = new OkHttpClient();
    private static final String SAVED_TEXT = "saved_text";
    SharedPreferences sPref;
    JSONObject courses;
    SwipeRefreshLayout swipeLayout;
    Integer bl;
    Integer blOute;
    RecyclerView listPaymentsMethod;

    ArrayList<SliderForPays> paymentMethods = new ArrayList<SliderForPays>();
    SliderForPaysAdapter boxAdapter;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pays_keys, null);
        fTrans = getActivity().getSupportFragmentManager().beginTransaction();
        addBalance = (Button)v.findViewById(R.id.addBalance);
        addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fTrans.replace(R.id.fr_place, new addBalancedFragments()).addToBackStack(null).commit();
            }
        });

        barcode = (ImageButton) v.findViewById(R.id.barCodeClick);
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), barcodeScanner.class);
                startActivity(intent);
            }
        });

        click_open_pays2 = (Button) v.findViewById(R.id.click_open_pays2);
        click_open_pays2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fTrans.replace(R.id.fr_place, new newPays()).addToBackStack(null).commit();
            }
        });

        courseDollar = (TextView)v.findViewById(R.id.courseDollar);
        courseRuble = (TextView)v.findViewById(R.id.courseRuble);
        courseEuro = (TextView)v.findViewById(R.id.courseEuro);

        vivod = (Button)v.findViewById(R.id.vivodButton);
        vivod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fTrans.replace(R.id.fr_place, new outeMoney()).addToBackStack(null).commit();
            }
        });

        tranzactionButton = (Button)v.findViewById(R.id.tranzactionButton);
        tranzactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fTrans.replace(R.id.fr_place, new historyTranzaction()).addToBackStack(null).commit();
            }
        });

        btnValutesTG = (ImageButton)v.findViewById(R.id.selectedVutesTG);
        btnValutesDL = (ImageButton)v.findViewById(R.id.selectedValutesDollars);
        btnValutesRB = (ImageButton)v.findViewById(R.id.selectedValutesRuble);
        btnValutesEUR = (ImageButton)v.findViewById(R.id.selectedValutesEuro);

        balanceDostup = (ImageView)v.findViewById(R.id.balanceDostup);
        balanceBlock = (ImageView)v.findViewById(R.id.balanceBlock);

        balance = (TextView)v.findViewById(R.id.blanceSumm);
        blockBalance = (TextView)v.findViewById(R.id.blockSumm);
        userName = (TextView) v.findViewById(R.id.userName);


        open_all_servicess = (Button) v.findViewById(R.id.open_all_servicess);
        open_all_servicess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fTrans.replace(R.id.fr_place, new allPays()).addToBackStack(null).commit();
            }
        });


        btnValutesTG.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setValutesTG();
                    }
                }
        );
        btnValutesDL.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setValutesDL();
                    }
                }
        );
        btnValutesRB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setBtnValutesRB();
                    }
                }
        );
        btnValutesEUR.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setValutesEur();
                    }
                }
        );

        sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipePullRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                swipeLayout.setRefreshing(false);
            }
        });

        boxAdapter = new SliderForPaysAdapter(this.getActivity(), paymentMethods, this);
        Button btnSendMoneyInContacts = (Button) v.findViewById(R.id.selectContactsButton);
        btnSendMoneyInContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkWriteExternalPermission() == true){
                    fTrans.replace(R.id.fr_place, new send_money_in_contacts()).addToBackStack(null).commit();
                }else{
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 123);
                    if(checkWriteExternalPermission() == true){
                        fTrans.replace(R.id.fr_place, new send_money_in_contacts()).addToBackStack(null).commit();
                    }
                }
            }
        });



        // настраиваем список
        listPaymentsMethod = (RecyclerView) v.findViewById(R.id.listSliderPayments);

        getDefoultData();
        updateData();
        SetDefoultValutes();
        return v;
    }
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fTrans.replace(R.id.fr_place, new send_money_in_contacts()).addToBackStack(null).commit();
        }
    }

    private boolean checkWriteExternalPermission() {
        String permission = Manifest.permission.READ_CONTACTS;
        int res = getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    public void getDefoultData(){
        final String savedText = sPref.getString("UserInfo", "");
        final String sliderServicess = sPref.getString("UserInfo", "");

        if(savedText.length() > 0){
            JSONObject respV = null;
            JSONObject resp = null;
            try {
                respV = new JSONObject(savedText).getJSONObject("exchangeRates");
                courses = respV;
                resp = new JSONObject(savedText);
                courseDollar.setText(respV.getString("USD"));
                courseEuro.setText(respV.getString("EUR"));
                courseRuble.setText(respV.getString("RUB"));
                userName.setText(resp.getString("nickName"));
                balance.setText(resp.getString("balance"));
                blockBalance.setText(resp.getString("balanceInBlock"));
                bl = (Integer) resp.getInt("balance");
                blOute = (Integer) resp.getInt("balanceInBlock");


                paymentMethods.clear();
                for(int i = 0; i < resp.getJSONArray("slider").length(); i++)
                {

                    JSONObject objects = resp.getJSONArray("slider").getJSONObject(i);
                    paymentMethods.add(new SliderForPays(objects.getString("name"), objects.getString("icon"), resp.getString("logoURL"), objects.getInt("id"), objects.put("icon",  objects.getString("icon"))));
                }
                listPaymentsMethod.setAdapter(boxAdapter);
                listPaymentsMethod.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


    public void updateData(){
        JSONObject userInfo = null;
        String UserId = null;
        String userPhone = null;
        String userPassword = null;
        try {
            userInfo = new JSONObject(loadText2("UserInfo"));
            UserId = userInfo.getString("ID");
            userPassword = userInfo.getString("password");
            userPhone = userInfo.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(UserId != null && userPhone != null && userPassword != null){
            String url = "https://api.indigo24.site/api/v2/check_auth";
            MediaType typed = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", userPhone)
                    .addFormDataPart("password", userPassword)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .post(requestBody)
                    .build();


            final String finalUserId = UserId;
            final String finalUserPassword = userPassword;
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e)   {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject rsp = null;
                                JSONObject respV = null;
                                try {
                                    rsp = new JSONObject(response.body().string());
                                    if(rsp.getBoolean("success") == true){
                                        respV = rsp.getJSONObject("exchangeRates");
                                        courses = respV;

                                        courseDollar.setText(respV.getString("USD"));
                                        courseEuro.setText(respV.getString("EUR"));
                                        courseRuble.setText(respV.getString("RUB"));
                                        balance.setText(rsp.getString("balance"));
                                        blockBalance.setText(rsp.getString("balanceInBlock"));

                                        bl = (Integer) rsp.getInt("balance");
                                        blOute = (Integer) rsp.getInt("balanceInBlock");


                                        paymentMethods.clear();
                                        for(int i = 0; i < rsp.getJSONArray("slider").length(); i++)
                                        {
                                            JSONObject objects = rsp.getJSONArray("slider").getJSONObject(i);
                                            paymentMethods.add(new SliderForPays(objects.getString("name"), rsp.getString("logoURL") + objects.getString("icon"), rsp.getString("logoURL"), objects.getInt("id"), objects.put("icon", rsp.getString("logoURL") + objects.getString("icon"))));
                                        }
                                        listPaymentsMethod.setAdapter(boxAdapter);
                                        listPaymentsMethod.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                                        SharedPreferences.Editor ed = sPref.edit();
                                        rsp.put("password", finalUserPassword);
                                        ed.putString("UserInfo", rsp.toString());
                                        ed.commit();
                                    }
                                    else{

                                        SharedPreferences.Editor editor = sPref.edit();
                                        editor.remove("autorize");
                                        editor.remove("UserInfo");
                                        editor.apply();

                                        Intent intent = new Intent(getActivity(),MainActivity.class);
                                        startActivity(intent);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }
    }




    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void setValutesTG(){
        btnValutesTG.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_valutes));
        btnValutesDL.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesRB.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesEUR.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        balanceDostup.setImageResource(R.drawable.ic_tgsim);
        balanceBlock.setImageResource(R.drawable.ic_tgsim);
        saveValuates("TG");
        Integer newBal = (Integer) bl;
        Integer newBalBlock = (Integer) blOute;
        balance.setText(Integer.toString(newBal));
        blockBalance.setText(Integer.toString(newBalBlock));
    }

    private void setValutesDL(){
        btnValutesTG.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesDL.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_valutes));
        btnValutesRB.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesEUR.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        balanceDostup.setImageResource(R.drawable.ic_dollarssim);
        balanceBlock.setImageResource(R.drawable.ic_dollarssim);
        saveValuates("USD");
        Integer newBal = null;
        Integer newBalBlock = null;
        try {
            newBal = (Integer) bl / courses.getInt("USD");
            newBalBlock = (Integer) blOute / courses.getInt("USD");
            balance.setText(Integer.toString(newBal));
            blockBalance.setText(Integer.toString(newBalBlock));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBtnValutesRB(){
        btnValutesTG.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesDL.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesRB.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_valutes));
        btnValutesEUR.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        balanceDostup.setImageResource(R.drawable.ic_rublesim);
        balanceBlock.setImageResource(R.drawable.ic_rublesim);
        saveValuates("RUB");
        Integer newBal = null;
        Integer newBalBlock = null;
        try {
            newBal = (Integer) bl / courses.getInt("RUB");
            newBalBlock = (Integer) blOute / courses.getInt("RUB");
            balance.setText(Integer.toString(newBal));
            blockBalance.setText(Integer.toString(newBalBlock));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setValutesEur(){
        btnValutesTG.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesDL.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesRB.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_background));
        btnValutesEUR.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_valutes));
        balanceDostup.setImageResource(R.drawable.ic_eurosim);
        balanceBlock.setImageResource(R.drawable.ic_eurosim);
        saveValuates("EUR");
        Integer newBal = null;
        Integer newBalBlock = null;
        try {
            newBal = (Integer) bl / courses.getInt("EUR");
            newBalBlock = (Integer) blOute / courses.getInt("EUR");
            balance.setText(Integer.toString(newBal));
            blockBalance.setText(Integer.toString(newBalBlock));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void saveValuates(String name) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, name.toString());
        ed.commit();
    }


    private String loadText(){
        String savedText = sPref.getString(SAVED_TEXT, "");
        return savedText;
    }

    private String loadText2(String name){
        String savedText = sPref.getString(name, "");
        return savedText;
    }

    private void SetDefoultValutes(){
        switch (loadText()){
            case "TG":
                setValutesTG();
                break;
            case  "USD":
                setValutesDL();
                break;
            case "RUB":
                setBtnValutesRB();
                break;
            case "EUR":
                setValutesEur();
                break;
            default:
                setValutesTG();
                break;
        }
    }


    public void onNoteClick(int position){
        Bundle data = new Bundle();
        data.putString("data", paymentMethods.get(position).dataForPay.toString());

        Fragment newFr = new only_payments_servise();
        newFr.setArguments(data);
        fTrans = getActivity().getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fr_place, newFr).addToBackStack(null).commit();
    }


}
