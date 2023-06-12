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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.bumptech.glide.Glide;
import com.example.diplom.Adapter.AdapterSummary;
import com.example.diplom.model.EstimatedTimeTab;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainTabFragment extends Fragment {

    TextView summaryInfo, detailTitle, summaryTo, summaryFrom;
    ImageView detailImage;
    FloatingActionButton addTabButton, editButton;
    String key = "";
    String imageUrl = "";
    String userId = "";
    RecyclerView taskViewList;
    AdapterSummary adapterSummary;
    List<EstimatedTimeTab> estimatedTimeTabs;
    LinearLayout containerLayout;
    AnyChartView anyChartView;
    final Handler handler = new Handler();
    Runnable runnable;
    Pie pie = null;
    String risks;

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

        summaryInfo = view.findViewById(R.id.summaryInfo);
        summaryFrom = view.findViewById(R.id.summaryFrom);
        summaryTo = view.findViewById(R.id.summaryTo);
        taskViewList = view.findViewById(R.id.taskList);
        detailImage = view.findViewById(R.id.detailImage);
        detailTitle = view.findViewById(R.id.detailTitle);
        addTabButton = view.findViewById(R.id.add_tab_button);
        editButton = view.findViewById(R.id.editButton);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            detailTitle.setText(bundle.getString("Name"));
            risks = bundle.getString("Additional Info");
            key = bundle.getString("Key");
            userId = bundle.getString("UserId");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        anyChartView = view.findViewById(R.id.anyChartView);
        anyChartView.setBackgroundColor("#fafafa");
        if (estimatedTimeTabs != null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
            taskViewList.setLayoutManager(gridLayoutManager);
            adapterSummary = new AdapterSummary(getContext(), estimatedTimeTabs);
            taskViewList.setAdapter(adapterSummary);
            createTextViews(estimatedTimeTabs);
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

    public void createTextViews(List<EstimatedTimeTab> estimatedTimeTabs) {
        summaryInfo.setText("The estimated project completion time including entered risks (" + risks + "%)");
        int sumFrom = 0;
        int sumTo = 0;
        for (EstimatedTimeTab estimatedTimeTab : estimatedTimeTabs) {
            sumFrom = sumFrom + estimatedTimeTab.getTabTimeFrom();
            sumTo = sumTo + estimatedTimeTab.getTabTimeTo();
        }
        summaryFrom.setText("from " + sumFrom + "h");
        summaryTo.setText("to " + sumTo + "h");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (estimatedTimeTabs != null) {
            final int delayMillis = 500;
            adapterSummary.notifyDataSetChanged();
            runnable = new Runnable() {
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
