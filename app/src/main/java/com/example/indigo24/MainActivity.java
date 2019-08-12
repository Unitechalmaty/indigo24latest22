package com.example.indigo24;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.load.engine.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    FrameLayout desc1;
    ImageView icon1;
    int step = 0;
    JSONArray country = new JSONArray();
    private final OkHttpClient client = new OkHttpClient();
    MaskedEditText phoneInput;
    public static EditText passwordInp;


    public static void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }







    public void changeAuth(){
        SharedPreferences sPrefed = getSharedPreferences("MY_PREFERENCESS", MODE_PRIVATE);
        String testAuth = sPrefed.getString("autorize", "");



        if(Boolean.valueOf(testAuth) == true){
            Intent intent = new Intent(getApplicationContext(), startTabs.class);
            finish();
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_autorization);
            getSupportActionBar().hide();
            menuCountry();
            FloatingActionButton fab = findViewById(R.id.autorizationButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendAuth();
                }
            });


            phoneInput = (MaskedEditText) findViewById(R.id.masked_edit_text);
            phoneInput.setImeActionLabel("Cerca", KeyEvent.KEYCODE_ENTER);
            phoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    sendAuth();
                    return false;
                }
            });



        }
    }


    public void sendAuth(){
        if(phoneInput.getMask().length() ==  phoneInput.getText().toString().replace("-", "").length()) {
            final ProgressBar progressAuth = (ProgressBar) findViewById(R.id.progressBarAutorization);
            progressAuth.setVisibility(View.VISIBLE);


            FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
            prel.setVisibility(View.VISIBLE);
            prel.setAlpha(0.0f);
            prel.animate()
                    .alpha(0.4f)
                    .setListener(null);


            phoneInput.setTextColor(Color.BLACK);
            String texted = phoneInput.getText().toString().replace(" ", "").replace("+", "").replace("-", "");
            String url = "https://api.indigo24.site/api/v2/check_reg";
            MediaType typed = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", texted)
                    .addFormDataPart("_token", "AibekQ")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(requestBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    backgroundThreadShortToast(getApplicationContext(), "Не удалось выполнить запрос!");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressAuth.setVisibility(View.GONE);
                            FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                            prel.setVisibility(View.GONE);
                            prel.setAlpha(0.4f);
                            prel.animate()
                                    .alpha(0.0f)
                                    .setListener(null);
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject resp;
                        try {
                            resp = new JSONObject(response.body().string());
                            if (resp.getBoolean("success") == true) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {
                                        passwordInp = (EditText) findViewById(R.id.PasswordInp);
                                        passwordInp.setVisibility(View.VISIBLE);
                                        passwordInp.setAlpha(0.0f);
                                        passwordInp.animate()
                                                .alpha(1.0f)
                                                .setListener(null);
                                        passwordInp.setImeActionLabel("Cerca", KeyEvent.KEYCODE_ENTER);
                                        passwordInp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                checkedAutzh(phoneInput.getText().toString(), passwordInp.getText().toString());
                                                return false;
                                            }
                                        });

                                        FloatingActionButton fab = findViewById(R.id.autorizationButton);
                                        fab.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                checkedAutzh(phoneInput.getText().toString(), passwordInp.getText().toString());

                                            }
                                        });


                                    }
                                });
                            } else {
                                startRegisteredProcess();
                            }
                        } catch (JSONException e) {
                            backgroundThreadShortToast(getApplicationContext(), "Не удалось проверить номер!");
                        }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressAuth.setVisibility(View.GONE);
                            FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                            prel.setVisibility(View.GONE);
                            prel.setAlpha(0.4f);
                            prel.animate()
                                    .alpha(0.0f)
                                    .setListener(null);
                        }
                    });
                }
            });
        }else {
            phoneInput.setTextColor(Color.RED);
        }
    }


    public void checkedAutzh(String number, final String password){
        if(phoneInput.getMask().length() ==  number.replace("+", "o").replace("-", "o").length()){
            phoneInput.setTextColor(Color.BLACK);
            final ProgressBar progressAuth = (ProgressBar) findViewById(R.id.progressBarAutorization);
            progressAuth.setVisibility(View.VISIBLE);
            FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
            prel.setVisibility(View.VISIBLE);
            prel.setAlpha(0.0f);
            prel.animate()
                    .alpha(0.4f)
                    .setListener(null);
            final String numPh = (String) number.replace("+", "").replace("-", "").replace(" ", "");
            String url = "https://api.indigo24.site/api/v2/check_auth";
            MediaType typed = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", numPh)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .post(requestBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e)   {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            backgroundThreadShortToast(getApplicationContext(), "Не удалось получить данные!");
                            progressAuth.setVisibility(View.GONE);
                            FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                            prel.setVisibility(View.VISIBLE);
                            prel.setAlpha(0.4f);
                            prel.animate()
                                    .alpha(9.0f)
                                    .setListener(null);
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject rsp = null;
                                try {
                                    rsp = new JSONObject(response.body().string());
                                } catch (JSONException e) {
                                    backgroundThreadShortToast(getApplicationContext(), "Не удалось получить данные!");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (rsp != null) {
                                    Boolean type = null;
                                    try {
                                        type = rsp.getBoolean("success");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (type != null) {
                                        if (type == false) {
                                            String message = null;
                                            try {
                                                message = (String) rsp.getString("message");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (message != null) {

                                                //Номер не зарегистрирвоан

                                                phoneInput = (MaskedEditText) findViewById(R.id.masked_edit_text);
                                                phoneInput.setImeActionLabel("Cerca", KeyEvent.KEYCODE_ENTER);
                                                phoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                        sendAuth();
                                                        return false;
                                                    }
                                                });
                                                EditText password = (EditText) findViewById(R.id.PasswordInp);
                                                password.setVisibility(View.GONE);
                                                password.setAlpha(1.0f);
                                                password.animate()
                                                        .alpha(0.0f)
                                                        .setListener(null);

                                                FloatingActionButton fab = findViewById(R.id.autorizationButton);
                                                fab.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        sendAuth();
                                                    }
                                                });


                                            } else {
                                                backgroundThreadShortToast(getApplicationContext(), "Вы указали не правильный пароль!");
                                                passwordInp.setTextColor(Color.RED);
                                            }

                                        }
                                        else{
                                            try {
                                                rsp.put("password", password);
                                                saveUserInfo("autorize", "true");
                                                passwordInp.setTextColor(Color.BLACK);
                                                saveUserInfo("UserInfo", rsp.toString());
                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            rsp.put("password", password);
                                            saveUserInfo("autorize", "true");
                                            passwordInp.setTextColor(Color.BLACK);
                                            saveUserInfo("UserInfo", rsp.toString().toString());
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    progressAuth.setVisibility(View.GONE);
                                    FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                                    prel.setVisibility(View.GONE);
                                    prel.setAlpha(0.4f);
                                    prel.animate()
                                            .alpha(0.0f)
                                            .setListener(null);
                                } else {
                                    progressAuth.setVisibility(View.GONE);
                                    FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                                    prel.setVisibility(View.GONE);
                                    prel.setAlpha(0.4f);
                                    prel.animate()
                                            .alpha(0.0f)
                                            .setListener(null);
                                }
                            }
                        });
                    }
                    else {

                                progressAuth.setVisibility(View.GONE);
                                FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                                prel.setVisibility(View.GONE);
                                prel.setAlpha(0.4f);
                                prel.animate()
                                        .alpha(0.0f)
                                        .setListener(null);
                    }
                }
            });
        }else{
            phoneInput.setTextColor(Color.RED);
        }

    }


    public void  startRegisteredProcess(){
        backgroundThreadShortToast(getApplicationContext(), "СМС Код 1111!");
    }


    private void saveUserInfo(String title, String name) {
        SharedPreferences sPref = getSharedPreferences("MY_PREFERENCESS", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(title, name.toString());
        ed.commit();
    }



    public void submitNewPassword(View playPause){
        if(phoneInput.getMask().length() == phoneInput.getText().toString().replace("+", "o").replace("-", "").length()){
            backgroundThreadShortToast(getApplicationContext(), "Нет соеденения с сервером");
            phoneInput.setTextColor(Color.BLACK);
        }
        else{
            backgroundThreadShortToast(getApplicationContext(), "Некоректно указан телефон!");
            phoneInput.setTextColor(Color.RED);
        }
    }


    Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    public void showPopup(View v) {

        final PowerMenu powerMenu = new PowerMenu.Builder(this)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT)
                .setMenuRadius(5f)
                .setMenuShadow(10f)
                .setTextColor(this.getResources().getColor(R.color.textGray))
                .setAnimation(MenuAnimation.SHOWUP_BOTTOM_RIGHT)
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(this.getResources().getColor(R.color.indigo))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                    }
                })
                .build();

        for(int i=0; i < country.length(); i++){
            try {
                final JSONObject data = country.getJSONObject(i);

                final Target mTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        Log.d("DEBUG", "onBitmapLoaded");
                        BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                        try {
                            PowerMenuItem item = new PowerMenuItem(data.getString("title"));
                            item.setTag(mBitmapDrawable);
                            powerMenu.addItem(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {
                        Log.d("DEBUG", "onPrepareLoad");
                    }
                };

                Uri uri = Uri.parse(data.getString("iconUrl"));

                Picasso.get().load(uri).into(mTarget);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        powerMenu.setWidth(400);
        powerMenu.showAsAnchorRightBottom(v);

    }


    public void changed(Boolean type){

        if(type == true) {
            changeAuth();

        }else {
            getSupportActionBar().hide();
            setContentView(R.layout.activity_start);
            icon1 = findViewById(R.id.iconStart1);
            desc1 = findViewById(R.id.descStart1);
            final Button indicator1 = (Button) findViewById(R.id.slideInd1);
            final Button indicator2 = (Button) findViewById(R.id.slideInd2);
            final Button indicator3 = (Button) findViewById(R.id.slideInd3);
            final TextView titleStart = (TextView) findViewById(R.id.titleStart);
            final TextView subTitleStart = (TextView) findViewById(R.id.subTitleStart);

            final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);

            icon1.startAnimation(animation2);
            desc1.startAnimation(animation);



            final ImageButton nextBtn = (ImageButton) findViewById(R.id.clickNexStart);
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    step++;

                    if(step == 1){
                        indicator1.setBackgroundResource(R.drawable.punctir);
                        indicator2.setBackgroundResource(R.drawable.punctir_active);
                        indicator3.setBackgroundResource(R.drawable.punctir);
                        desc1.setBackgroundResource(R.drawable.background_start_help2);

                        Toast.makeText(getApplicationContext(), "fvds", Toast.LENGTH_SHORT).show();
                        ImageViewAnimatedChange(getApplicationContext(), icon1, R.drawable.ic_help2);
                        titleStart.setText("Чат спартнерами и друзьями");
                        subTitleStart.setText("Удобная система обмена сообщениями, аудио и видео вызовы");
                    }
                    else if(step == 2){
                        indicator1.setBackgroundResource(R.drawable.punctir);
                        indicator2.setBackgroundResource(R.drawable.punctir);
                        indicator3.setBackgroundResource(R.drawable.punctir_active);
                        desc1.setBackgroundResource(R.drawable.background_start_help3);
                        ImageViewAnimatedChange(getApplicationContext(), icon1, R.drawable.ic_help3);
                        titleStart.setText("Оплачивай услуги в один клик");
                        subTitleStart.setText("Огромный выбор услуг всегда под рукой.");
                        nextBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    }
                    else if(step == 3){
                        setFersted();
                        changeAuth();
                    }
                }
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

    public void razborArray(final Context context, final String jsn) {
        if (context != null && jsn != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    try {
                        final JSONObject resp = new JSONObject(jsn);
                        JSONArray respArr = resp.getJSONArray("result");
                        for(int i = 0; i < respArr.length(); i++){
                            final Integer ct = (Integer) i;
                            final JSONObject object = respArr.getJSONObject(i);
                            final JSONObject newCountry = new JSONObject();
                            try {
                                if(i == 0){
                                    ImageButton flag = (ImageButton) findViewById(R.id.flagCountry);
                                    String url = (String) resp.getString("flagURL") + object.getString("icon");
                                    Picasso.get()
                                            .load(url)
                                            .resize(50,  38)
                                            .centerCrop()
                                            .into(flag);

                                    flag.setAlpha(0.f);
                                    flag.animate()
                                            .alpha(1.0f)
                                            .setListener(null);
                                }
                                Picasso.get()
                                        .load(resp.getString("flagURL") + object.getString("icon"))
                                        .resize(50,  38)
                                        .centerCrop()
                                        .into(new Target() {
                                                  @Override
                                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                      try {
                                                          newCountry.put("icon", bitmap);
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }
                                                  }
                                                  @Override
                                                  public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                                  }

                                                  @Override
                                                  public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                  }
                                              }
                                        );
                                newCountry.put("iconUrl", resp.getString("flagURL") + object.getString("icon"));
                                newCountry.put("title",  object.getString("title"));
                                newCountry.put("prefix",  object.getString("prefix"));
                                newCountry.put("mask",  object.getString("mask"));
                                country.put(newCountry);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final ProgressBar progressAuth = (ProgressBar) findViewById(R.id.progressBarAutorization);
                    progressAuth.setVisibility(View.VISIBLE);
                    FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                    prel.setVisibility(View.VISIBLE);
                    prel.setAlpha(0.4f);
                    prel.animate()
                            .alpha(0.0f)
                            .setListener(null);
                }
            });
        }
    }

    public void menuCountry(){
        final ProgressBar progressAuth = (ProgressBar) findViewById(R.id.progressBarAutorization);
        progressAuth.setVisibility(View.VISIBLE);
        FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
        prel.setVisibility(View.VISIBLE);
        prel.setAlpha(0.0f);
        prel.animate()
                .alpha(0.4f)
                .setListener(null);


        String url = "https://api.indigo24.site/api/v2/get_countries";
        MediaType typed = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("test", "1")
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
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressAuth.setVisibility(View.VISIBLE);
                        FrameLayout prel = (FrameLayout) findViewById(R.id.loadProgress);
                        prel.setVisibility(View.VISIBLE);
                        prel.setAlpha(0.4f);
                        prel.animate()
                                .alpha(0.0f)
                                .setListener(null);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                razborArray(getApplicationContext(), response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SharedPreferences sPrefSrt = getPreferences(MODE_PRIVATE);
        Boolean savedText = (Boolean) sPrefSrt.getBoolean("ferstStart", false);
        changed(savedText);

    }

    public void setFersted(){
        SharedPreferences sPrefSrt2 = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPrefSrt2.edit();
        ed.putBoolean("ferstStart", true);
        ed.commit();
    }

}
