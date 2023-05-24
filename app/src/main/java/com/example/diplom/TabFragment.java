package com.example.diplom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private List<String> expandableListTitles;
    private Map<String, List<String>> expandableListDetails;

    private Button addListViewButton;

    private int listViewCount = 3;
    private String title;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    public static TabFragment newInstance(String title) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);

        expandableListDetail = ExpandableListDataPump.getData();

        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        expandableListAdapter = new CustomExpandableListAdapter
                (getContext(), expandableListTitle, expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);
        return view;
    }

//    private void addNewListView() {
//        listViewCount++;
//        String newTitle = "Список " + listViewCount;
//        expandableListTitles.add(newTitle);
//        expandableListDetails.put(newTitle, new ArrayList<String>());
//        expandableListAdapter.notifyDataSetChanged();
//    }
}