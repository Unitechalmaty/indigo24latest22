package com.example.indigo24;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.indigo24.adapterForList.adapter.ViwePageAdapterPays;
import com.google.android.material.tabs.TabLayout;

public class allPays extends Fragment {
    @StringRes
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_pays, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle("Все платежи");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = (TabLayout) v.findViewById(R.id.tabsPays);
        viewPager = (ViewPager) v.findViewById(R.id.viepage);

        ViwePageAdapterPays adapterPays = new ViwePageAdapterPays(getChildFragmentManager());
        adapterPays.AddFragment(new myPays(), "Мои платежи");
        adapterPays.AddFragment(new categoryForPays(), "Все платежи");
        adapterPays.AddFragment(new trazaction(), "История платежей");



        viewPager.setAdapter(adapterPays);
        tabLayout.setupWithViewPager(viewPager);






        return v;
    }
}
