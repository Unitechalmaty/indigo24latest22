package com.example.indigo24;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indigo24.adapterForList.adapter.AdapterForHistory;
import com.example.indigo24.adapterForList.adapter.AdapterMyPays;
import com.example.indigo24.adapterForList.objected.Hostory;
import com.example.indigo24.adapterForList.objected.myPay;

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

public class trazaction extends Fragment {
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Hostory> myHistory = new ArrayList<Hostory>();
    AdapterForHistory boxAdapter;
    ListView listingHistory;
    SharedPreferences sPref;
    FrameLayout fr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_trazaction, container, false);

        boxAdapter = new AdapterForHistory(this.getActivity(), myHistory);
        listingHistory = (ListView) v.findViewById(R.id.listingsHistory);
        sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);
        fr = (FrameLayout)v.findViewById(R.id.showKvit);
        loadData();




        updateList(getContext());

        return v;
    }

    private void loadData(){
        final SharedPreferences sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);
        String savedText = sPref.getString("history", "");
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
                    myHistory.clear();
                    mysPays = rsp.getJSONArray("history");
                    for(int i = 0; i < mysPays.length(); i++)
                    {

                        JSONObject hh = mysPays.getJSONObject(i);
                        myHistory.add(new Hostory(hh));
                    }
                    updateList(getContext());


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

                if (response.isSuccessful())
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject rsp = null;
                            String logoUrlDef = null;
                            try {
                                rsp = new JSONObject(response.body().string());
                                logoUrlDef = rsp.getString("logoURL");
                                SharedPreferences.Editor ed = sPref.edit();
                                ed.putString("history", rsp.toString());
                                ed.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (rsp != null) {
                                JSONArray mysPays = null;
                                try {
                                    myHistory.clear();
                                    mysPays = rsp.getJSONArray("history");
                                    for (int i = 0; i < mysPays.length(); i++) {
                                        JSONObject hh = mysPays.getJSONObject(i);
                                        myHistory.add(new Hostory(hh));
                                    }

                                    updateList(getContext());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            }
        });
    }

    public void updateList(final Context context) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    listingHistory.setAdapter(boxAdapter);
                    listingHistory.setItemsCanFocus(false);
                    listingHistory.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                            Hostory arr = myHistory.get(position);
                            ((FrameLayout) getActivity().findViewById(R.id.showKvit)).setVisibility(View.VISIBLE);
                            ((FrameLayout) getActivity().findViewById(R.id.backKvit)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((FrameLayout) getActivity().findViewById(R.id.showKvit)).setVisibility(View.GONE);

                                }
                            });

                            try {
                                String titile = arr.data.getString("provider");
                                ((TextView) getActivity().findViewById(R.id.titleKvit)).setText(titile);
                                ((TextView) getActivity().findViewById(R.id.descKvit)).setText(arr.data.getString("description"));
                                ((TextView) getActivity().findViewById(R.id.dateKvit)).setText(arr.data.getString("created_at"));
                                ((TextView) getActivity().findViewById(R.id.summKvit)).setText(arr.data.getString("amount") + " тг");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        };
                    });
                };
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
}
