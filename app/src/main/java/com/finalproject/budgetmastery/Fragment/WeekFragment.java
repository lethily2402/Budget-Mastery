package com.finalproject.budgetmastery.Fragment;

import android.graphics.ColorSpace;
import android.icu.text.NumberFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class WeekFragment extends Fragment {
    private BarChart bcChart;
    private TextView textViewTotalSpending, tvCategoryMostSpending;
    private ProgressBar pcProcessing;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_week, container, false);

        bcChart = view.findViewById(R.id.bc_chart);
        textViewTotalSpending = view.findViewById(R.id.textView10);
        tvCategoryMostSpending = view.findViewById(R.id.TextView15);
        pcProcessing = view.findViewById(R.id.pb_processing);

        setupBarChart();
        retrieveSpending();

        return view;
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

        YAxis leftAxis = bcChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setGranularityEnabled(true);
    }


    private void retrieveSpending() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().child("addkhoanchi").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalMoneyCurrentWeek = 0;
                int totalMoneyPreviousWeek = 0;
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
                            if (isDateInCurrentWeek(date)) {
                                totalMoneyCurrentWeek += amount;

                                /*String category = khoanChi.getTvTitle();
                                if (category != null) {
                                    int currentSpending = categorySpending.getOrDefault(category, 0);
                                    categorySpending.put(category, currentSpending + amount);
                                }*/
                            } else if (isDateInPreviousWeek(date)) {
                                totalMoneyPreviousWeek += amount;
                            }
                        }
                    }
                }

                /*String maxCategory = "";
                int maxAmount = 0;
                for (Map.Entry<String, Integer> entry : categorySpending.entrySet()) {
                    if (entry.getValue() > maxAmount) {
                        maxCategory = entry.getKey();
                        maxAmount = entry.getValue();
                    }
                }*/

                String formattedTotalAmount = formatCurrency(totalMoneyCurrentWeek);
//                String formattedMaxCategorySpending = maxCategory + ": " + formatCurrency(maxAmount);

                textViewTotalSpending.setText(formattedTotalAmount);
//                tvCategoryMostSpending.setText(formattedMaxCategorySpending);
                pcProcessing.setVisibility(View.GONE);

                loadBarChartData(totalMoneyPreviousWeek, totalMoneyCurrentWeek);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void loadBarChartData(int totalMoneyPreviousWeek, int totalMoneyCurrentWeek) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        // Scale the values to thousands for better visualization
        float previousWeekSpending = totalMoneyPreviousWeek / 1000f;
        float currentWeekSpending = totalMoneyCurrentWeek / 1000f;

        entries.add(new BarEntry(0f, previousWeekSpending));
        entries.add(new BarEntry(1f, currentWeekSpending));

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(new int[]{0xFFEEEEEE});
        barDataSet.setDrawValues(true); // Show values on the bars
        barDataSet.setHighlightEnabled(false);

        BarData data = new BarData(barDataSet);
        bcChart.setData(data);

        // Update labels to indicate the scale
        String[] labels = new String[]{"Tuần trước", "Tuần này"};
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


    private String formatCurrency(int amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    public static boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }

    private boolean isDateInPreviousWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int weekOfYear = currentCalendar.get(Calendar.WEEK_OF_YEAR) - 1;
        int year = currentCalendar.get(Calendar.YEAR);

        if (weekOfYear == 0) {
            weekOfYear = currentCalendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
            year -= 1;
        }

        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeekOfYear = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);

        return weekOfYear == targetWeekOfYear && year == targetYear;
    }
}
