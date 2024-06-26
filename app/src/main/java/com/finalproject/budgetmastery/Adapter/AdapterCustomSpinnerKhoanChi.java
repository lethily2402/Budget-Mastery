package com.finalproject.budgetmastery.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.R;

import java.util.List;

public class AdapterCustomSpinnerKhoanChi extends ArrayAdapter<ModelListKhoanChi> {
    private final LayoutInflater inflater;
    private final List<ModelListKhoanChi> items;

    public AdapterCustomSpinnerKhoanChi(Context context, List<ModelListKhoanChi> items) {
        super(context, R.layout.custom_spinner_item, items);
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_spinner_item, parent, false);
        }

        ModelListKhoanChi item = getItem(position);

        if (item != null) {
            ImageView imageView = convertView.findViewById(R.id.spinner_image);
            TextView textView = convertView.findViewById(R.id.spinner_text);

            Glide.with(getContext())
                    .load(Uri.parse(item.getImageUri()))
                    .into(imageView);

            textView.setText(item.getTxt_title());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
