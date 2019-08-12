package com.example.indigo24;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.indigo24.adapterForList.adapter.AdapterForCategoryServicess;
import com.example.indigo24.adapterForList.objected.CategoryService;

import org.json.JSONArray;
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

public class newPays extends Fragment {
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<CategoryService> myPays = new ArrayList<CategoryService>();
    AdapterForCategoryServicess boxAdapter;
    ListView listingMyPays;
    SharedPreferences sPref;
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_pays, container, false);


        toolbar = (Toolbar) v.findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle("Категории");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boxAdapter = new AdapterForCategoryServicess(this.getActivity(), myPays);
        listingMyPays = (ListView) v.findViewById(R.id.listCategory);

        sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);

        loadData();

        return v;
    }

    private void loadData(){
        final SharedPreferences sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);
        String savedText = sPref.getString("categoryPays", "");
        if(savedText.length() > 0){
            JSONObject rsp = null;
            String logoUrlDef = null;
            try {
                rsp = new JSONObject(savedText);
                logoUrlDef = rsp.getString("logoURL");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(rsp != null){
                JSONArray mysPays = null;
                try {
                    myPays.clear();
                    mysPays = rsp.getJSONArray("categoryPays");
                    for(int i = 0; i < mysPays.length(); i++)
                    {
                        JSONObject hh = mysPays.getJSONObject(i);
                        myPays.add(new CategoryService(hh.getString("name"), hh.getString("icon"), hh.getJSONArray("dataServices")));
                    }
                    listingMyPays.setAdapter(boxAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        JSONObject userId = null;
        String idUser = null;
        try {
            userId = new JSONObject(sPref.getString("UserInfo", ""));
            idUser =userId.getString("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = "https://api.indigo24.site/api/v2/list";
        MediaType typed = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("unic", "e7ffe7b1a323a1c76f18e8816bb6ea087cd18393_1563778257_40")
                .addFormDataPart("id", idUser )
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
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject rsp = null;
                            String logoUrlDef = null;
                            try {
                                rsp = new JSONObject(response.body().string());
                                logoUrlDef = rsp.getString("logoURL");
                                SharedPreferences.Editor ed = sPref.edit();
                                ed.putString("myPays", rsp.toString());
                                ed.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if(rsp != null){
                                JSONArray mysPays = null;
                                try {
                                    myPays.clear();
                                    mysPays = rsp.getJSONArray("categoryPays");
                                    for(int i = 0; i < mysPays.length(); i++)
                                    {
                                        JSONObject hh = mysPays.getJSONObject(i);
                                        myPays.add(new CategoryService(hh.getString("name"), logoUrlDef + hh.getString("icon"), hh.getJSONArray("dataServices")));
                                    }
                                    listingMyPays.setAdapter(boxAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

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
}
