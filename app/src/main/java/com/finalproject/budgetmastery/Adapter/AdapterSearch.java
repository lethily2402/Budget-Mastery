package com.finalproject.budgetmastery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;

import java.util.List;

public class AdapterSearch extends ArrayAdapter<ModelListHome> {
    private List<ModelListHome> itemList;
    private Context context;

    public AdapterSearch(Context context, int home_list_item_by_date, List<ModelListHome> itemList) {
        super(context, R.layout.home_list_item_by_date, itemList);
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_list_item_by_date, parent, false);
        }

        ModelListHome currentItem = getItem(position);
        if (currentItem != null) {
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView tvTitle = convertView.findViewById(R.id.tvTitle);
            TextView tvAmount = convertView.findViewById(R.id.tvAmount);
            TextView tvDate = convertView.findViewById(R.id.tvDate);
            TextView tvDay = convertView.findViewById(R.id.tvDay);

            // Load image using Glide
            if (currentItem.getImageUri() != null && !currentItem.getImageUri().isEmpty()) {
                Glide.with(context).load(currentItem.getImageUri()).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.thucpham_icon);
            }

            // Set text views
            tvTitle.setText(currentItem.getTvTitle());
            tvAmount.setText(currentItem.getTvAmount());
            tvDate.setText(currentItem.getTvDate());
            tvDay.setText(currentItem.getTvDay());
        }

        return convertView;
    }

    public void updateList(List<ModelListHome> newList) {
        itemList.clear();
        itemList.addAll(newList);
        notifyDataSetChanged();
    }
}
