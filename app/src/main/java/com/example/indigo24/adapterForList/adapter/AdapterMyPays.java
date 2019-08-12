package com.example.indigo24.adapterForList.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.indigo24.R;
import com.example.indigo24.adapterForList.objected.Contacts;
import com.example.indigo24.adapterForList.objected.myPay;
import com.example.indigo24.only_payments_servise;
import com.example.indigo24.profile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterMyPays extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<myPay> objects;
    Map<String, String> map = new HashMap<String, String>();

    public AdapterMyPays(Context context, ArrayList<myPay> MyPay) {
        ctx = context;
        objects = MyPay;
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

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.listed_for_my_pays, parent, false);
            final myPay p = getProduct(position);

            ImageView img = (ImageView) view.findViewById(R.id.iconForListing);
            Picasso.get()
                    .load(p.defoultUrl + p.icon)
                    .into(img);

            TextView txt = (TextView) view.findViewById(R.id.namePaysed);
            txt.setText(p.name);

            ImageButton btnPop = (ImageButton) view.findViewById(R.id.clickPopUp);


            if(p.showParametrs == false){
                btnPop.setVisibility(View.GONE);
                LinearLayout clicks = (LinearLayout) view.findViewById(R.id.clickToOad);
                clicks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager manager = ((FragmentActivity) ctx).getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();

                        Fragment newFr = new only_payments_servise();
                        Bundle data = new Bundle();
                        data.putString("data", p.dataForPay.toString());
                        newFr.setArguments(data);
                        transaction.add(R.id.fr_place,newFr).addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
            else{
                btnPop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        PopupMenu popup = new PopupMenu(ctx, view2);

                        SpannableString s = new SpannableString("Повторить платеж");
                        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                        final MenuItem item = popup.getMenu().add("").setTitle(s);


                        SpannableString s2 = new SpannableString("Удалить");
                        s2.setSpan(new ForegroundColorSpan(Color.RED), 0, s2.length(), 0);
                        final MenuItem item2 = popup.getMenu().add("").setTitle(s2);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            popup.setForceShowIcon(true);
                        }

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                FragmentManager manager = ((FragmentActivity) ctx).getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();

                                Fragment newFr = new only_payments_servise();
                                Bundle data = new Bundle();
                                data.putString("data", p.dataForPay.toString());
                                newFr.setArguments(data);
                                transaction.add(R.id.fr_place,newFr).addToBackStack(null);
                                transaction.commit();
                                return true;
                            }
                        });
                        popup.show();



                    }
                });
            }
            TextView txt2 = (TextView) view.findViewById(R.id.defoultId);
            if(p.defoultValue == null){
                txt2.setVisibility(View.GONE);
            }else{
                txt2.setText(p.defoultValue);
            }



            TextView txt3 = (TextView) view.findViewById(R.id.txtSumm);
            if(p.summ != "none"){
                txt3.setText(p.summ);
            }
            else{
                txt3.setVisibility(View.GONE);
            }


        }


        return view;
    }



    myPay getProduct(int position) {
        return ((myPay) getItem(position));
    }
}
