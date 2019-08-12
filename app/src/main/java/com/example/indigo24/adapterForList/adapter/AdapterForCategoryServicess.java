package com.example.indigo24.adapterForList.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.indigo24.R;
import com.example.indigo24.adapterForList.objected.CategoryService;
import com.example.indigo24.listServicesForCategoryPage;
import com.example.indigo24.only_payments_servise;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterForCategoryServicess extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<CategoryService> objects;
    Map<String, String> map = new HashMap<String, String>();

    public AdapterForCategoryServicess(Context context, ArrayList<CategoryService> categoryService) {
        ctx = context;
        objects = categoryService;
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
            view = lInflater.inflate(R.layout.list_category_viwe, parent, false);
            final CategoryService p = getProduct(position);


            ImageView img = (ImageView) view.findViewById(R.id.imagesed);

            Picasso.get()
                    .load(p.icon)
                    .into(img);

            TextView txt = (TextView) view.findViewById(R.id.titleCategories);
            txt.setText(p.name);


            TextView txt2 = (TextView) view.findViewById(R.id.countCategory);
            txt2.setText(Integer.toString(p.servicess.length()));


            LinearLayout click = (LinearLayout) view.findViewById(R.id.clicked);
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager manager = ((FragmentActivity) ctx).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    Fragment newFr = new listServicesForCategoryPage();
                    Bundle data = new Bundle();
                    data.putString("data", p.servicess.toString());
                    data.putString("name", p.name);
                    newFr.setArguments(data);
                    transaction.add(R.id.fr_place,newFr).addToBackStack(null);
                    transaction.commit();
                }
            });

        }


        return view;
    }



    CategoryService getProduct(int position) {
        return ((CategoryService) getItem(position));
    }
}
