package com.example.indigo24;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class startTabs extends AppCompatActivity {
    private TextView mTextMessage;
    FragmentTransaction fTrans;

    chat frag1;
    lenta frag2;
    profile frag3;
    pays frag4;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fTrans = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    fTrans.replace(R.id.fr_place, new chat()).commit();
                    return true;
                case R.id.navigation_lenta:
                    fTrans.replace(R.id.fr_place, new lenta()).commit();
                    return true;
                case R.id.navigation_profile:
                    fTrans.replace(R.id.fr_place, new profile()).commit();
                    return true;
                case R.id.navigation_pays:
                    fTrans.replace(R.id.fr_place, new pays()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tabs);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != 0){
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    public void showedPop(View v){
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);

        for(int i = 1; i < 32; i++){
            final MenuItem item = popup.getMenu().add(i + " день месяца");
        }


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "Не коректный URL", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }
}
