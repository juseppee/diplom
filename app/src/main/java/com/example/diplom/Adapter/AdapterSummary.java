package com.example.diplom.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;
import com.example.diplom.model.EstimatedTimeTab;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;

public class AdapterSummary extends RecyclerView.Adapter<NwViewHolder> {

    private Context context;
    private List<EstimatedTimeTab> estimatedTimeTabList;
    private List<String> colors = Arrays.asList("#64b5f6","#1976d3", "#ffd54f", "#ef6c00");

    public AdapterSummary(Context context, List<EstimatedTimeTab> estimatedTimeTabList) {
        this.context = context;
        this.estimatedTimeTabList = estimatedTimeTabList;
    }

    @Override
    public NwViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, parent, false);
        return new NwViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NwViewHolder holder, int position) {
        holder.subtaskName.setText(estimatedTimeTabList.get(position).getTabName());
        holder.subtaskTo.setText(estimatedTimeTabList.get(position).getTabTimeTo().toString());
        holder.subtaskFrom.setText(estimatedTimeTabList.get(position).getTabTimeFrom().toString());
        if (position < 4) {
            holder.verticalLine.setBackgroundColor(Color.parseColor(colors.get(position)));
        } else {
            holder.verticalLine.setBackgroundColor((int)(Math.random() * 0x1000000));
        }
    }

    @Override
    public int getItemCount() {
        return estimatedTimeTabList.size();
    }
}

class NwViewHolder extends RecyclerView.ViewHolder {

    TextView subtaskName, subtaskTo, subtaskFrom;
    public View verticalLine;

    public NwViewHolder(@NonNull View itemView) {
        super(itemView);
            subtaskTo = itemView.findViewById(R.id.name);
            subtaskFrom = itemView.findViewById(R.id.category);
            subtaskName = itemView.findViewById(R.id.status);
            verticalLine = itemView.findViewById(R.id.verticalLine);
    }
}


