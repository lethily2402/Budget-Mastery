package com.finalproject.budgetmastery.Fragment;



import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class WeekFragment extends Fragment {
    private BarChart bcChart;
    private TextView textViewTotalSpending, tvCategoryMostSpending;
    private ProgressBar pcProcessing;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference userKhoanChiRef;

    private int maxSpending = 0;
    private String maxSpendingCategory = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);

        bcChart = view.findViewById(R.id.bc_chart);
        textViewTotalSpending = view.findViewById(R.id.textView10);
        tvCategoryMostSpending = view.findViewById(R.id.TextView15);
        pcProcessing = view.findViewById(R.id.pb_processing);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userKhoanChiRef = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(userId).child("addkhoanchi");

            setupBarChart();
            retrieveSpending();
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

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
        userKhoanChiRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalMoneyCurrentWeek = 0;
                int totalMoneyPreviousWeek = 0;
                maxSpending = 0; // Reset max spending for each retrieval
                maxSpendingCategory = "";

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date;

                // Maps to store total spending by category
                HashMap<String, Integer> categorySpendingMap = new HashMap<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelListHome khoanChi = dataSnapshot.getValue(ModelListHome.class);
                    if (khoanChi != null) {
                        String dateInString = khoanChi.getTvDate();
                        try {
                            date = formatter.parse(dateInString);
                        } catch (ParseException e) {
                            Log.e("ParseException", "Failed to parse date", e);
                            continue; // Skip this iteration if date parsing fails
                        }

                        if (khoanChi.getTvAmount() != null) {
                            int amount = Integer.parseInt(khoanChi.getTvAmount());

                            // Track maximum spending category
                            if (amount > maxSpending) {
                                maxSpending = amount;
                                maxSpendingCategory = khoanChi.getTvTitle(); // Adjust based on your ModelListHome structure
                            }

                            // Aggregate spending by category
                            String category = khoanChi.getTvTitle();
                            if (categorySpendingMap.containsKey(category)) {
                                int currentSpending = categorySpendingMap.get(category);
                                categorySpendingMap.put(category, currentSpending + amount);
                            } else {
                                categorySpendingMap.put(category, amount);
                            }

                            if (isDateInCurrentWeek(date)) {
                                totalMoneyCurrentWeek += amount;
                            } else if (isDateInPreviousWeek(date)) {
                                totalMoneyPreviousWeek += amount;
                            }
                        }
                    }
                }

                // Calculate total spending for the most spending category
                int totalSpendingForMaxCategory = categorySpendingMap.getOrDefault(maxSpendingCategory, 0);

                String formattedTotalAmount = formatCurrency(totalMoneyCurrentWeek);
                textViewTotalSpending.setText(formattedTotalAmount);

                String mostSpendingText = maxSpendingCategory + ": " + formatCurrency(totalSpendingForMaxCategory);
                tvCategoryMostSpending.setText(mostSpendingText);

                pcProcessing.setVisibility(View.GONE);

                loadBarChartData(totalMoneyPreviousWeek, totalMoneyCurrentWeek);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                Toast.makeText(requireContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBarChartData(int totalMoneyPreviousWeek, int totalMoneyCurrentWeek) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        float previousWeekSpending = totalMoneyPreviousWeek / 1000f;
        float currentWeekSpending = totalMoneyCurrentWeek / 1000f;

        entries.add(new BarEntry(0f, previousWeekSpending));
        entries.add(new BarEntry(1f, currentWeekSpending));

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(new int[]{0xFFEEEEEE});
        barDataSet.setDrawValues(true); // Show values on bars
        barDataSet.setHighlightEnabled(false);

        BarData data = new BarData(barDataSet);
        bcChart.setData(data);

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

        bcChart.invalidate(); // Refresh chart
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