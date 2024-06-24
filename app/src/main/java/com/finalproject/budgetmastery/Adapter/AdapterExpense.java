package com.finalproject.budgetmastery.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;

import java.util.List;

public class AdapterExpense extends ArrayAdapter<ModelListHome> {
    private Context context;
    private int resource;
    private List<ModelListHome> expenses;
    public AdapterExpense(Context context, int resource, List<ModelListHome> expenses) {
        super(context, resource, expenses);
        this.context = context;
        this.resource = resource;
        this.expenses = expenses;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        ModelListHome expense  = expenses.get(position);

        if (expense != null) {
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView textViewDate = convertView.findViewById(R.id.tvDate);
            TextView textViewDay = convertView.findViewById(R.id.tvDay);
            TextView textViewTitle = convertView.findViewById(R.id.tvTitle);
            TextView textViewAmount = convertView.findViewById(R.id.tvAmount);

            // Hiển thị dữ liệu lên views
            textViewDate.setText(expense.getTvDate());
            textViewDay.setText(expense.getTvDay());
            textViewTitle.setText(expense.getTvTitle());
            textViewAmount.setText(expense.getTvAmount());

            if (expense.getImageUri() != null && !expense.getImageUri().isEmpty()) {
                Glide.with(getContext()).load(expense.getImageUri()).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.thucpham_icon);
            }
            Log.d("AdapterListHome", "Image URI: " + expense.getImageUri());

            convertView.setBackgroundColor(Color.TRANSPARENT);

        }

        return convertView;
    }
}

