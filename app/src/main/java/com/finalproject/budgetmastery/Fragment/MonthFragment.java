package com.finalproject.budgetmastery.Fragment;

import android.icu.text.NumberFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MonthFragment extends Fragment {
    private BarChart bcChart;
    private TextView textViewTotalSpending, tvCategoryMostSpending;
    private ProgressBar pcProcessing;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        bcChart = view.findViewById(R.id.bc_chart);
        textViewTotalSpending = view.findViewById(R.id.textView10);
        pcProcessing = view.findViewById(R.id.pb_processing);


        setupBarChart();
        retrieveSpending();

        return view;
    }

    private void retrieveSpending() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().child("addkhoanchi").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalMoneyCurrentMonth = 0;
                int totalMoneyPreviousMonth = 0;
                Map<String, Integer> categorySpending = new HashMap<>();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date;
                String dateInString;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelListKhoanChi khoanChi = dataSnapshot.getValue(ModelListKhoanChi.class);
                    if (khoanChi != null) {
                        dateInString = khoanChi.getTvDate();
                        try {
                            assert dateInString != null;
                            date = formatter.parse(dateInString);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        if (khoanChi.getTvAmount() != null) {
                            int amount = Integer.parseInt(khoanChi.getTvAmount());
                            if (isDateInCurrentMonth(date)) {
                                totalMoneyCurrentMonth += amount;
                            } else if (isDateInPreviousMonth(date)) {
                                totalMoneyPreviousMonth += amount;
                            }
                        }
                    }
                }
                String formattedTotalAmount = formatCurrency(totalMoneyCurrentMonth);

                textViewTotalSpending.setText(formattedTotalAmount);
                pcProcessing.setVisibility(View.GONE);

                loadBarChartData(totalMoneyPreviousMonth, totalMoneyCurrentMonth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void setupBarChart() {
        bcChart.setDrawBarShadow(false);
        bcChart.setDrawValueAboveBar(true);
        bcChart.getDescription().setEnabled(false);
        bcChart.setMaxVisibleValueCount(2);
        bcChart.setPinchZoom(false);
        bcChart.setDoubleTapToZoomEnabled(false);
        bcChart.setScaleEnabled(false);
        bcChart.setScaleXEnabled(false);
        bcChart.setScaleYEnabled(false);
        bcChart.setDragEnabled(false);
        bcChart.setDrawGridBackground(false);
        bcChart.getAxisLeft().setDrawGridLines(false);
        bcChart.getAxisRight().setDrawGridLines(false);
        bcChart.getXAxis().setDrawGridLines(false);
        bcChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bcChart.getXAxis().setGranularity(1f);
        bcChart.getXAxis().setGranularityEnabled(true);
        bcChart.getAxisLeft().setEnabled(true);
        bcChart.getAxisRight().setEnabled(false);
        bcChart.getLegend().setEnabled(false);

        // Adjust the Y-axis to accommodate the range of values
        YAxis leftAxis = bcChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Start at zero
        leftAxis.setGranularity(1f); // Interval size
        leftAxis.setGranularityEnabled(true);
    }


    private void loadBarChartData(int totalMoneyPreviousMonth, int totalMoneyCurrentMonth) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        // Scale the values to thousands for better visualization
        float previousMonthSpending = totalMoneyPreviousMonth / 1000f;
        float currentMonthSpending = totalMoneyCurrentMonth / 1000f;

        entries.add(new BarEntry(0f, previousMonthSpending));
        entries.add(new BarEntry(1f, currentMonthSpending));

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(new int[]{0xFFEEEEEE});
        barDataSet.setDrawValues(true); // Show values on the bars
        barDataSet.setHighlightEnabled(false);

        BarData data = new BarData(barDataSet);
        bcChart.setData(data);

        // Update labels to indicate the scale
        String[] labels = new String[]{"Tháng trước", "Tháng này"};
        bcChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < labels.length) {
                    return labels[(int) value];
                }
                return "";
            }
        });

        bcChart.invalidate(); // Refresh the chart
    }

    public static boolean isDateInCurrentMonth(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);

        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int targetMonth = targetCalendar.get(Calendar.MONTH);

        return currentMonth == targetMonth;
    }

    public static boolean isDateInPreviousMonth(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.MONTH, -1); // Move to previous month
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);

        int previousMonth = currentCalendar.get(Calendar.MONTH);
        int targetMonth = targetCalendar.get(Calendar.MONTH);

        return previousMonth == targetMonth;
    }

    private String formatCurrency(int amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}