package com.example.indigo24;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.indigo24.adapterForList.adapter.AdapterMyPays;
import com.example.indigo24.adapterForList.objected.myPay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;


public class listServicesForCategoryPage extends Fragment {
    Toolbar toolbar;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<myPay> myPays = new ArrayList<myPay>();
    AdapterMyPays boxAdapter;
    ListView listingMyPays;
    SharedPreferences sPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_services_for_category_page, container, false);

        String name = getArguments().getString("name");
        JSONArray objects = null;
        try {
            objects = new JSONArray(getArguments().getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle(name);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boxAdapter = new AdapterMyPays(this.getActivity(), myPays);
        listingMyPays = (ListView) v.findViewById(R.id.listViweMyPays);
        sPref = getActivity().getSharedPreferences("MY_PREFERENCESS", MainActivity.MODE_PRIVATE);

        if(objects != null){
            for(int i = 0; i < objects.length(); i++)
            {
                JSONObject hh = null;
                try {
                    hh = objects.getJSONObject(i);
                    myPays.add(new myPay(hh.getString("name"), hh.getString("icon"), "https://api.indigo24.site/logos/", hh.getInt("id"), hh.put("icon", "https://api.indigo24.site/logos/" + hh.getString("icon")),  "none", null, false));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            listingMyPays.setAdapter(boxAdapter);
        }
        return v;
    }
}
