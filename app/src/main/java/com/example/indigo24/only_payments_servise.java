package com.example.indigo24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.example.indigo24.adapterForList.adapter.ContactsAdapter;
import com.example.indigo24.adapterForList.adapter.InpInServiceAdapter;
import com.example.indigo24.adapterForList.objected.Contacts;
import com.example.indigo24.adapterForList.objected.InpService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class only_payments_servise extends Fragment {
    Toolbar toolbar;
    ArrayList<InpService> listedInp = new ArrayList<InpService>();
    InpInServiceAdapter boxAdapter;
    ListView listInputs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_only_payments_servise, container, false);
        String data = getArguments().getString("data");

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnSended = (Button)v.findViewById(R.id.buttonSendMoney);
        btnSended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Метод отсутствует", Toast.LENGTH_SHORT).show();
            }
        });



        try {
            JSONObject dataForPay = new JSONObject(data);
            toolbar.setTitle("  " + dataForPay.getString("name"));


            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable d = new BitmapDrawable(getResources(), bitmap);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(d);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {}
            };

            Picasso.get()
                    .load(dataForPay.getString("icon"))
                    .resize(70, 70)
                    .transform(new ContactsAdapter.CircleTransform())
                    .centerCrop()
                    .into(target);
            boxAdapter = new InpInServiceAdapter(this.getActivity(), listedInp);
            // настраиваем список
            listInputs = v.findViewById(R.id.inputedArr);

            JSONArray inputs = dataForPay.getJSONArray("inputs");
            for(int i = 0; i < inputs.length(); i++){
                JSONObject inp = inputs.getJSONObject(i);
                String defVal = "none";
                try {
                    defVal = inp.getString("defaultValue");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                listedInp.add(new InpService(inp.getString("name"), inp.getBoolean("qr"), inp.getJSONObject("autoPay"), defVal ,  inp.getString("typeInput"), inp.getString("placeholder"), inp.getString("mask")));
            }

            listInputs.setAdapter(boxAdapter);
            final LinearLayout mPays = (LinearLayout)v.findViewById(R.id.mounthPays);

            Switch checkAuto = v.findViewById(R.id.switcher);


            checkAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        mPays.setVisibility(View.VISIBLE);
                    }
                    else{
                        mPays.setVisibility(View.GONE);
                    }
                }
            });

            JSONObject aut = dataForPay.getJSONObject("autoPay");
            if(aut.getBoolean("boll") == true){
                Button btnDay = v.findViewById(R.id.textAutoPayMonts);
                btnDay.setText(aut.getString("dayOfPay") + "день месяца");

                EditText summedAutoPays = v.findViewById(R.id.summedAutoPayts);
                summedAutoPays.setText(aut.getString("summOfPay") + " тг.");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return v;
    }

   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        listedInp.get(Integer.valueOf(data.getStringExtra("position"))).defoultValue = data.getStringExtra("qrCode");
       listInputs.setAdapter(boxAdapter);
    }



}
