package com.finalproject.budgetmastery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.Model.ModelListKhoanThu;
import com.finalproject.budgetmastery.R;

import java.util.List;

public class AdapterListKhoanThu extends ArrayAdapter<ModelListKhoanThu> {
    private Context context;
    private int resource;
    private List<ModelListKhoanThu> items;

    public AdapterListKhoanThu(@NonNull Context context, int resource, @NonNull List<ModelListKhoanThu> items) {
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
        ModelListKhoanThu item = getItem(position);
        if (item != null) {
            imageIcon.setImageResource(item.getImage_icon());
            txtTitle.setText(item.getTxt_title());
        }
        return convertView;
    }
}
