package com.finalproject.budgetmastery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterFrameKhoanChi extends ArrayAdapter<ModelListLKhoanChi> {
    Activity context;
    int resouce;


    public AdapterFrameKhoanChi(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = (Activity) context;
        this.resouce = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = this.context.getLayoutInflater();
        View customview = layoutInflater.inflate(this.resouce, null);
        ImageView image_icon = (ImageView) customview.findViewById(R.id.image_icon);
        TextView txt_title = (TextView) customview.findViewById(R.id.txt_title);


        ModelListLKhoanChi items = getItem(position);

        image_icon.setImageResource(items.getImage_icon());
        txt_title.setText(items.getTxt_title());
        return customview;

    }
}




