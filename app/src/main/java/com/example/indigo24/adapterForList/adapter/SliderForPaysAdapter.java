package com.example.indigo24.adapterForList.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.indigo24.R;
import com.example.indigo24.adapterForList.objected.SliderForPays;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SliderForPaysAdapter extends RecyclerView.Adapter<SliderForPaysAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<SliderForPays> imageModelArrayList;
    private OnNoteListener mOnNoteListener;

    public SliderForPaysAdapter(Context ctx, ArrayList<SliderForPays> imageModelArrayList, OnNoteListener onNoteListener){

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
        this.mOnNoteListener = onNoteListener;
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
    public SliderForPaysAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_horizontal_slider_viwe, parent, false);
        MyViewHolder holder = new MyViewHolder(view, mOnNoteListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(SliderForPaysAdapter.MyViewHolder holder, int position) {



        Picasso.get()
                .load(imageModelArrayList.get(position).icon)
                .into(holder.iv);

        holder.time.setText(imageModelArrayList.get(position).name);

    }


    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time;
        ImageView iv;
        OnNoteListener onNoteListener;

        public MyViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.textHorizontalSlider);
            iv = (ImageView) itemView.findViewById(R.id.iamgeHorizontalList);
            this.onNoteListener = onNoteListener;


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}