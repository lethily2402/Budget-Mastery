package com.finalproject.budgetmastery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdapterFrameHome extends ArrayAdapter<ModelListHome> {

    Activity context;
    int resouce;

    public AdapterFrameHome(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = (Activity) context;
        this.resouce = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = this.context.getLayoutInflater();
        View customview = layoutInflater.inflate(this.resouce, null);
        TextView tvDay = (TextView) customview.findViewById(R.id.tvDay);
        TextView tvDate = (TextView) customview.findViewById(R.id.tvDate);
        TextView tvTitle = (TextView) customview.findViewById(R.id.tvTitle);
        TextView tvAmount = (TextView) customview.findViewById(R.id.tvAmount);

        ModelListHome items = getItem(position);

        tvDate.setText(items.getTvDate());
        tvDay.setText(items.getTvDay());
        tvTitle.setText(items.getTvTitle());
        tvAmount.setText(items.getTvAmount());


        return customview;
    }
}
