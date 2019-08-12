package com.example.indigo24.adapterForList.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.StrictMode;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indigo24.R;
import com.example.indigo24.adapterForList.objected.InpService;
import com.example.indigo24.barcodeScanner;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InpInServiceAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<InpService> objects;
    Map<String, String> map = new HashMap<String, String>();
    EditText summedPerevodInp;
    public InpInServiceAdapter(Context context, ArrayList<InpService> inputs) {
        ctx = context;
        objects = inputs;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if( convertView == null ) {
            // используем созданные, но не используемые view

            if (view == null) {
                view = lInflater.inflate(R.layout.list_input_in_servicess, parent, false);
            }

            final InpService p = getProduct(position);


            final ImageButton qr = (ImageButton) view.findViewById(R.id.buttonQr);



            if (p.qr == true) {
                qr.setVisibility(View.VISIBLE);
                qr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ctx, barcodeScanner.class);
                        intent.putExtra("type", "onlyServise");
                        intent.putExtra("index", Integer.toString(position));
                        ((Activity) ctx).startActivityForResult(intent, 1);
                    }
                });
            } else {
                qr.setVisibility(View.GONE);
            }

            MaskedEditText summedPerevod = new MaskedEditText.Builder(ctx)
                    .mask(p.mask)
                    .notMaskedSymbol("*")
                    .build();
            summedPerevod.setMaskedText(p.mask.replaceAll("\\d","").replace("*","_"));
            summedPerevod.setBackgroundResource(R.drawable.style_inputs2);
            summedPerevod.setPadding(50,30, 50,30);
            summedPerevod.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            TextView txt = (TextView) view.findViewById(R.id.titled);
            txt.setText(p.placeHolder);

            Toast.makeText(ctx,p.defoultValue, Toast.LENGTH_SHORT).show();
            if(p.defoultValue !=  "none"){
                summedPerevod.setMaskedText(p.defoultValue.replaceAll("[^0-9]", ""));
            }

           FrameLayout linearLayout = (FrameLayout) view.findViewById(R.id.laioutParenmts);
            linearLayout.addView(summedPerevod);
        }
        return view;
    }



    InpService getProduct(int position) {
        return ((InpService) getItem(position));
    }
}
