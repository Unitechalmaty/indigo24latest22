package com.example.indigo24;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indigo24.adapterForList.adapter.ContactsAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class summ_send_user_money extends Fragment {
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summ_send_user_money, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Отправить деньги");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        String getUserName = getArguments().getString("name");
        String getUserPhone = getArguments().getString("phone");
        String colorBack = getArguments().getString("colorBack");
        String colorText = getArguments().getString("colorText");
        String avatar = getArguments().getString("avatar");

        toolbar.setSubtitle("Пользователю: " + getUserName);


        GradientDrawable shape = new GradientDrawable();


        shape.setShape(GradientDrawable.RECTANGLE);
        if(colorBack == null){
            colorBack= "#3fa1d2";
        }
        shape.setColor(Color.parseColor(colorBack));
        shape.setCornerRadius(200);

        FrameLayout shapeAvatar = (FrameLayout) v.findViewById(R.id.backgroundImageUser);
        shape.setStroke(3, Color.BLACK);
        shapeAvatar.setBackground(shape);
        ImageView ava = (ImageView) v.findViewById(R.id.avatarSend);
        TextView textefFerstSim = (TextView)v.findViewById(R.id.ferstSimNameUser);
        if(avatar != "none"){
            textefFerstSim.setVisibility(View.GONE);
            ava.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(avatar)
                    .resize(150, 150)
                    .transform(new ContactsAdapter.CircleTransform())
                    .centerCrop()
                    .into(ava);
        }
        else{
            textefFerstSim.setVisibility(View.VISIBLE);
            ava.setVisibility(View.GONE);
            textefFerstSim.setText(getUserName.substring(0, 2));
            textefFerstSim.setTextColor(Color.parseColor(colorText));
        }

        Button sendBTNmoney = (Button) v.findViewById(R.id.buttonSendMoney);
        sendBTNmoney.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Метод отсутствует", Toast.LENGTH_SHORT).show();
                    }
                }
        );



        return v;
    }

}
