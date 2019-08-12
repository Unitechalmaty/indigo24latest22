package com.example.indigo24.adapterForList.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.indigo24.R;
import com.example.indigo24.adapterForList.objected.CategoryService;
import com.example.indigo24.adapterForList.objected.Hostory;
import com.example.indigo24.listServicesForCategoryPage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterForHistory extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Hostory> objects;
    Map<String, String> map = new HashMap<String, String>();

    public AdapterForHistory(Context context, ArrayList<Hostory> history) {
        ctx = context;
        objects = history;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.listings_for_history, parent, false);
            final Hostory p = getProduct(position);


            ImageView img = view.findViewById(R.id.iconForListing);

            TextView txt1 = view.findViewById(R.id.namePaysed);
            TextView txt2 = view.findViewById(R.id.defoultId);
            TextView txt3 = view.findViewById(R.id.summs);
            TextView txt4 = view.findViewById(R.id.txtSumm);


            try {
                String logo = "https://api.indigo24.site/logos/"+p.data.getString("logo");
                Picasso.get()
                        .load(logo)
                        .into(img);

                txt1.setText(p.data.getString("description"));
                txt2.setText(p.data.getString("created_at"));
                txt3.setText(p.data.getString("amount"));
                txt4.setText(p.data.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


        return view;
    }



    Hostory getProduct(int position) {
        return ((Hostory) getItem(position));
    }
}

