package com.example.diplom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.Adapter.AdapterTime;
import com.example.diplom.model.EstimateLine;
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
    List<EstimateLine> estimateLineList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
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

        estimateLineList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        recyclerView = view.findViewById(R.id.recyclerViewSub);
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
        adapter = new AdapterTime(getContext(), estimateLineList);
        recyclerView.setAdapter(adapter);

        dialog.show();
        System.out.println("databaseReference " + databaseReference.toString());

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                estimateLineList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    EstimateLine estimateLine = itemSnapshot.getValue(EstimateLine.class);
                    estimateLine.setKey(itemSnapshot.getKey());
                    estimateLineList.add(estimateLine);
                }
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


}