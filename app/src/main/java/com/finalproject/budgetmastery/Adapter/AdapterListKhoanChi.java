package com.finalproject.budgetmastery.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.R;

import java.util.List;

public class AdapterListKhoanChi extends ArrayAdapter<ModelListKhoanChi> {
    private Context context;
    private int resource;
    private List<ModelListKhoanChi> items;

    public AdapterListKhoanChi(@NonNull Context context, int resource, @NonNull List<ModelListKhoanChi> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        ImageView imageIcon = convertView.findViewById(R.id.image_icon);
        TextView txtTitle = convertView.findViewById(R.id.txt_title);
        ModelListKhoanChi item = getItem(position);
        if (item != null) {
            Log.d("FirebaseData", "Image URI: " + item.getImageUri());
            Glide.with(context)
                    .load(Uri.parse(item.getImageUri()))
                    .into(imageIcon);


            txtTitle.setText(item.getTxt_title());
        }

        return convertView;
    }
}