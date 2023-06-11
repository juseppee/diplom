package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.bumptech.glide.Glide;
import com.example.diplom.model.EstimatedTimeTab;
import com.github.clans.fab.FloatingActionButton;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainTabFragment extends Fragment{

    TextView detailDesc, detailTitle, detailInfo;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";
    String userId = "";
    List<EstimatedTimeTab> estimatedTimeTabs;
    LinearLayout containerLayout;
    AnyChartView anyChartView;
    final Handler handler = new Handler();
    Runnable runnable;
    Pie pie = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MainTabFragment newInstance() {
        return new MainTabFragment();
    }

    public void setEstimatedTimeTabs(List<EstimatedTimeTab> estimatedTimeTabs) {
        this.estimatedTimeTabs = estimatedTimeTabs;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        detailDesc = view.findViewById(R.id.detailDesc);
        detailImage = view.findViewById(R.id.detailImage);
        detailTitle = view.findViewById(R.id.detailTitle);
        detailInfo = view.findViewById(R.id.detailInfo);
        deleteButton = view.findViewById(R.id.deleteButton);
        editButton = view.findViewById(R.id.editButton);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Name"));
            detailInfo.setText(bundle.getString("Additional Info"));
            key = bundle.getString("Key");
            userId = bundle.getString("UserId");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        anyChartView = view.findViewById(R.id.anyChartView);
        anyChartView.setBackgroundColor("#fafafa");
        containerLayout = view.findViewById(R.id.container_layout);
        if (estimatedTimeTabs != null) {
            createTextViews(estimatedTimeTabs, containerLayout);
            setupChartView(estimatedTimeTabs);
        }
        return view;
    }

    private void setupChartView(List<EstimatedTimeTab> estimatedTimeTabs) {
        pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (EstimatedTimeTab estimatedTimeTab : estimatedTimeTabs) {
            dataEntries.add(new ValueDataEntry(estimatedTimeTab.getTabName(), estimatedTimeTab.getTabTimeTo()));
        }

        pie.data(dataEntries);
        pie.title("Estimated Time");
        anyChartView.setChart(pie);
        anyChartView.setBackgroundColor("#fafafa");
    }

    public void createTextViews(List<EstimatedTimeTab> estimatedTimeTabs, LinearLayout containerLayout) {
        containerLayout.removeAllViews();

        for (EstimatedTimeTab tab : estimatedTimeTabs) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setPadding(18, 18, 18, 18);
            textView.setPaddingRelative(55, 0, 55, 0);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textView.setText("На реализацию вкладки " + tab.getTabName() + " уйдет от  " + tab.getTabTimeFrom() + " до " + tab.getTabTimeTo());

            containerLayout.addView(textView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (estimatedTimeTabs != null) {
            createTextViews(estimatedTimeTabs, containerLayout);
            final int delayMillis = 500;

            runnable = new Runnable(){
                public void run() {
                    List<DataEntry> dataEntries = new ArrayList<>();
                    for (EstimatedTimeTab estimatedTimeTab : estimatedTimeTabs) {
                        dataEntries.add(new ValueDataEntry(estimatedTimeTab.getTabName(), estimatedTimeTab.getTabTimeTo()));
                    }
                    pie.data(dataEntries);

                    handler.postDelayed(this, delayMillis);
                }
            };
            anyChartView.setBackgroundColor("#fafafa");
            handler.postDelayed(runnable, delayMillis);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
