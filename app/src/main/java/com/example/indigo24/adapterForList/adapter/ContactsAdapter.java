package com.example.indigo24.adapterForList.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.indigo24.R;
import com.example.indigo24.adapterForList.objected.Contacts;

public class ContactsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Contacts> objects;
    Map<String, String> map = new HashMap<String, String>();

    public ContactsAdapter(Context context, ArrayList<Contacts> contacts) {
        ctx = context;
        objects = contacts;
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
            view = lInflater.inflate(R.layout.list_contacts_viwe, parent, false);
        }

        final Contacts p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tvName)).setText(p.name);

        if(p.phone == null){
            ((TextView) view.findViewById(R.id.tvPhone)).setVisibility(View.GONE);
        }
        else{
            ((TextView) view.findViewById(R.id.tvPhone)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tvPhone)).setText(p.phone);
        }




        if(p.avatar != "none"){
            ((FrameLayout) view.findViewById(R.id.dynamicName)).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.avatar)).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.iconListCont)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.ferstSimvol)).setVisibility(View.VISIBLE);
            final ImageView image = (ImageView) view.findViewById(R.id.avatar);


            final View finalView = view;
            final View finalView1 = view;
            Picasso.get()
                    .load(p.avatar)
                    .resize(100, 100)
                    .transform(new CircleTransform())
                    .centerCrop()
                    .into(image);

            ((TextView) view.findViewById(R.id.ferstSimvol)).setText(p.name.substring(0, Math.min(p.name.length(), 2)).toUpperCase() + "");
            GradientDrawable shape = new GradientDrawable();


            shape.setShape(GradientDrawable.RECTANGLE);
            if(p.colorBack == null){
                p.colorBack = "#3fa1d2";
            }
            shape.setColor(Color.parseColor(p.colorBack));
            shape.setCornerRadius(100);
            ((FrameLayout) view.findViewById(R.id.dynamicName)).setBackground(shape);
            if(p.colorText == null){
                p.colorText = "#f9f9f9";
            }
            ((TextView) view.findViewById(R.id.ferstSimvol)).setTextColor(Color.parseColor(p.colorText));

        }
        else{
            ((ImageView) view.findViewById(R.id.avatar)).setVisibility(View.GONE);
            ((FrameLayout) view.findViewById(R.id.dynamicName)).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.iconListCont)).setVisibility(View.GONE);


            if(p.type != 0){
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                p.colorBack = "#f9f9f9";
                shape.setColor(Color.parseColor(p.colorBack));
                shape.setCornerRadius(100);
                ((FrameLayout) view.findViewById(R.id.dynamicName)).setBackground(shape);
                ((TextView) view.findViewById(R.id.ferstSimvol)).setTextColor(Color.parseColor(p.colorText));


                ((ImageView) view.findViewById(R.id.iconListCont)).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.ferstSimvol)).setVisibility(View.GONE);

                if(p.type == 1){
                    ((ImageView) view.findViewById(R.id.iconListCont)).setImageResource(R.drawable.ic_add_user);
                }

                if(p.type == 2){
                    ((ImageView) view.findViewById(R.id.iconListCont)).setImageResource(R.drawable.ic_share);
                }
            }
            else{
                ((TextView) view.findViewById(R.id.ferstSimvol)).setText(p.name.substring(0, Math.min(p.name.length(), 2)).toUpperCase() + "");
                GradientDrawable shape = new GradientDrawable();


                shape.setShape(GradientDrawable.RECTANGLE);
                if(p.colorBack == null){
                    p.colorBack = "#3fa1d2";
                }
                shape.setColor(Color.parseColor(p.colorBack));
                shape.setCornerRadius(100);
                ((FrameLayout) view.findViewById(R.id.dynamicName)).setBackground(shape);
                if(p.colorText == null){
                    p.colorText = "#f9f9f9";
                }
                ((TextView) view.findViewById(R.id.ferstSimvol)).setTextColor(Color.parseColor(p.colorText));
            }
        }

        return view;
    }



    Contacts getProduct(int position) {
        return ((Contacts) getItem(position));
    }
}
