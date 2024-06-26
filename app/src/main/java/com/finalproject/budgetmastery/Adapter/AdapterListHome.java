
package com.finalproject.budgetmastery.Adapter;

import android.content.Context;
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
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;

import java.util.List;

public class AdapterListHome extends ArrayAdapter<ModelListHome> {

    private final LayoutInflater inflater;
    private final List<ModelListHome> items;
    private final Context context;


    public AdapterListHome(Context context, int resource, List<ModelListHome> items) {
        super(context, resource, items);
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.home_list_item_by_date, parent, false);
        }

        ModelListHome item = getItem(position);

        if (item != null) {
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView textViewDate = convertView.findViewById(R.id.tvDate);
            TextView textViewDay = convertView.findViewById(R.id.tvDay);
            TextView textViewTitle = convertView.findViewById(R.id.tvTitle);
            TextView textViewAmount = convertView.findViewById(R.id.tvAmount);


            textViewDate.setText(item.getTvDate());
            textViewDay.setText(item.getTvDay());
            textViewTitle.setText(item.getTvTitle());
            textViewAmount.setText(item.getTvAmount());

            if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
                Glide.with(getContext()).load(item.getImageUri()).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.thucpham_icon);
            }
            Log.d("AdapterListHome", "Image URI: " + item.getImageUri());



        }

        return convertView;
    }
}
