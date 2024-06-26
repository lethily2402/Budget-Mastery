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
import java.util.Map;

public class MonthFragment extends Fragment {
    private BarChart bcChart;
    private TextView textViewTotalSpending;
    private ProgressBar pcProcessing;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference userKhoanChiRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        bcChart = view.findViewById(R.id.bc_chart);
        textViewTotalSpending = view.findViewById(R.id.textView10);
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
                int totalMoneyCurrentMonth = 0;
                int totalMoneyPreviousMonth = 0;
                Map<String, Integer> categorySpending = new HashMap<>();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date;

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

                            if (isDateInCurrentMonth(date)) {
                                totalMoneyCurrentMonth += amount;

                                // Track spending by category
                                String category = khoanChi.getTvTitle();
                                categorySpending.put(category, categorySpending.getOrDefault(category, 0) + amount);
                            } else if (isDateInPreviousMonth(date)) {
                                totalMoneyPreviousMonth += amount;
                            }
                        }
                    }
                }

                // Find the category with the highest spending
                String maxSpendingCategory = null;
                int maxSpendingAmount = 0;
                for (Map.Entry<String, Integer> entry : categorySpending.entrySet()) {
                    if (entry.getValue() > maxSpendingAmount) {
                        maxSpendingCategory = entry.getKey();
                        maxSpendingAmount = entry.getValue();
                    }
                }

                String formattedTotalAmount = formatCurrency(totalMoneyCurrentMonth);
                textViewTotalSpending.setText(formattedTotalAmount);

                if (maxSpendingCategory != null) {
                    String maxSpendingCategoryText = maxSpendingCategory + ": " + formatCurrency(maxSpendingAmount);
                    TextView textViewMaxSpendingCategory = getView().findViewById(R.id.TextView14); // Adjust this if the ID is different
                    textViewMaxSpendingCategory.setText(maxSpendingCategoryText);
                }

                pcProcessing.setVisibility(View.GONE);

                loadBarChartData(totalMoneyPreviousMonth, totalMoneyCurrentMonth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                Toast.makeText(requireContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadBarChartData(int totalMoneyPreviousMonth, int totalMoneyCurrentMonth) {
        ArrayList<BarEntry> entries = new ArrayList<>();
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
