package com.finalproject.budgetmastery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.finalproject.budgetmastery.Model.ModelListKhoanThu;
import com.finalproject.budgetmastery.R;

import java.util.List;

public class AdapterCustomSpinnerKhoanThu extends ArrayAdapter<ModelListKhoanThu> {
    private Context context;
    private List<ModelListKhoanThu> items;

    public AdapterCustomSpinnerKhoanThu(Context context, List<ModelListKhoanThu> items) {
        super(context, R.layout.custom_spinner_item, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.spinner_image);
        TextView textView = convertView.findViewById(R.id.spinner_text);

        ModelListKhoanThu currentItem = getItem(position);

        if (currentItem != null) {
            textView.setText(currentItem.getTxt_title());
            Glide.with(context).load(currentItem.getImageUri()).into(imageView);
        }

        return convertView;
    }
}
