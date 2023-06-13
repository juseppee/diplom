package com.example.diplom;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.diplom.Adapter.AdapterTime;
import com.example.diplom.model.EstimateSubTask;
import com.example.diplom.model.EstimatedTimeTab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TabFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    List<EstimateSubTask> estimateSubTaskList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    TextView tabInfo, tabSummaryFrom, tabSummaryTo;
    AdapterTime adapter;
    String userId = "";
    String formattedDate = "";
    private static final String ARG_TITLE = "title";
    private static final String FORM_DATE = "FormattedDate";

    private String title;

    public static TabFragment newInstance(String title, String formattedDate) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(FORM_DATE, formattedDate);;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            formattedDate = getArguments().getString(FORM_DATE);
        }

        estimateSubTaskList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        recyclerView = view.findViewById(R.id.recyclerViewSub);
        tabInfo = view.findViewById(R.id.textViewTitle);
        floatingActionButton = view.findViewById(R.id.floatingActionButtonSubtask);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Estimates").child(userId).child(formattedDate)
                .child("tabs").child(title);
        adapter = new AdapterTime(getContext(), estimateSubTaskList);
        recyclerView.setAdapter(adapter);

        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                estimateSubTaskList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    EstimateSubTask estimateSubTask = itemSnapshot.getValue(EstimateSubTask.class);
                    estimateSubTask.setKey(itemSnapshot.getKey());
                    estimateSubTaskList.add(estimateSubTask);
                }
                setSummary(estimateSubTaskList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                dialog.dismiss();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity(), UploadSubtaskActivity.class)
                        .putExtra("FormattedDate", formattedDate)
                        .putExtra("TabName", title)
                        .putExtra("UserId", userId);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSummary(estimateSubTaskList);
    }

    public void setSummary(List<EstimateSubTask> estimateSubTaskList) {
        int sum = 0;
        for (EstimateSubTask tab : estimateSubTaskList) {
            sum = sum + Integer.parseInt(tab.getSubtaskTo());
            tabInfo.setText("Completion will take up to " + sum + " hours");
        }
    }
}