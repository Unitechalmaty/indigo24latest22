package com.example.indigo24;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.indigo24.adapterForList.adapter.ContactsAdapter;
import com.example.indigo24.adapterForList.objected.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class send_money_in_contacts extends Fragment {
    ArrayList<Contacts> contacts = new ArrayList<Contacts>();
    ContactsAdapter boxAdapter;
    ListView listContacts;
    JSONArray contscts;
    String responed;
    ProgressBar progressBar;
    Toolbar toolbar;
    FragmentTransaction fTrans;

    private final OkHttpClient client = new OkHttpClient();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_send_money_in_contacts, container, false);


        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle("Выбор контактов");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setProgress(10);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        super.onStart();
        boxAdapter = new ContactsAdapter(this.getActivity(), contacts);
        // настраиваем список
        listContacts = (ListView) v.findViewById(R.id.listContacts);




        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillData();
    }

    void updateList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(listContacts != null){
                    listContacts.setAdapter(boxAdapter);
                    listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(i == 0){
                                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                                startActivity(intent);
                            }
                            else if(i == (contacts.size() -1)){
                                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                                Uri url = Uri.parse("https://indigo24.com/img/logo.png");
                                imageUris.add(url);

                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                                shareIntent.setType("image/*");
                                startActivity(Intent.createChooser(shareIntent, "Оплачивай товары и услуги в один клик! Общайяся с друзьями, отправляй им деньги не выходя из чата. Все это и многое другое в приложении INDIGO24."));
                            }
                            else{
                                Bundle data = new Bundle();//create bundle instance
                                data.putString("phone", contacts.get(i).phone);
                                data.putString("name", contacts.get(i).name);
                                data.putString("avatar", contacts.get(i).avatar);
                                data.putString("colorBack", contacts.get(i).colorBack);
                                data.putString("colorText", contacts.get(i).colorText);

                                Fragment newFr = new summ_send_user_money();
                                newFr.setArguments(data);
                                fTrans = getActivity().getSupportFragmentManager().beginTransaction();
                                fTrans.replace(R.id.fr_place, newFr).addToBackStack(null).commit();
                            }

                        }
                    });
                    progressBar.setProgress(100);
                    progressBar.setVisibility(ProgressBar.GONE);

                    toolbar.setSubtitle(Integer.toString(contacts.size() - 2));
                }
            }
        });
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




    public void obrcnt(JSONObject objects, JSONObject allData) throws JSONException {
        String nameCont;
        String avatar;
        String colorBack = null;
        String colorText = null;
        Boolean indigos = false;
        try{
            nameCont = (String) objects.getString("name");
            indigos = (Boolean) objects.getBoolean("registered");
        } catch (JSONException e){
            nameCont = (String) objects.getString("phone");
            e.printStackTrace();
        }

        try{
            colorBack = (String) objects.getString("colorBack");
        }catch (JSONException e){
            colorBack = null;
        }

        try{
            colorText = (String) objects.getString("colorText");
        }catch (JSONException e){
            colorText = null;
        }

        try {
            avatar =  (String) allData.getString("avatarURL")+ objects.getString("avatar");
        }catch (JSONException e){
            avatar = (String) "none";
            e.printStackTrace();
        }

        if(colorBack == null){
            colorBack = "#3fa1d2";
        }

        if(colorText == null){
            colorText = "#f9f9f9";
        }

        if(indigos){
            contacts.add(new Contacts(nameCont, objects.getString("phone"), R.drawable.ic_launcher_background, false, colorBack, colorText, avatar, allData.getString("defaultAvatar"), 0));
        }
    }


    void fillData() {
        progressBar.setProgress(30);
        SharedPreferences sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);
        String savedText = sPref.getString("contacts", "");
        if(savedText.length() > 2){
            responed = savedText;
            startLoad();
        }


        contscts = new JSONArray();

        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        // Loop Through All The Numbers
        while (phones.moveToNext()) {

            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            // Cleanup the phone number
            phoneNumber = phoneNumber.replaceAll("[()\\s-]+", "");

            // Enter Into Hash Map
            JSONObject cnt = new JSONObject();
            try {
                cnt.put("name", name);
                cnt.put("phone", phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            contscts.put(cnt);
        }

        phones.close();

        progressBar.setProgress(40);

        String url = "https://api.indigo24.site/api/v2/get_contacts";
        MediaType typed = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("unic", "e7ffe7b1a323a1c76f18e8816bb6ea087cd18393_1563778257_40")
                .addFormDataPart("contacts",  contscts.toString())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e)   {
                e.printStackTrace();
                backgroundThreadShortToast(getContext(), "Ошибка соеденения с сервером!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    contscts = new JSONArray();
                    responed = (String) response.body().string();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            startLoad();
                        }
                    });
                }
            }
        });

    }

    public void startLoad(){
        progressBar.setProgress(70);
        try {
            JSONArray jResp = new JSONObject(responed).getJSONArray("result");
            JSONObject allData = new JSONObject((responed));
            contacts.clear();
            contacts.add(new Contacts("Новый контакт",  null, R.drawable.ic_launcher_background, false, "#3fa1d2", "#ecf0f1", "none", null, 1));
            for(int i = 0; i < jResp.length(); i++)
            {

                JSONObject objects = jResp.getJSONObject(i);
                obrcnt(objects, allData);

            }
            contacts.add(new Contacts("Пригласить друзей",  null, R.drawable.ic_launcher_background, false, "#3fa1d2", "#ecf0f1", "none", null, 2));

            SharedPreferences sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString("contacts", responed);
            ed.commit();
            updateList();
            responed = "";

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
