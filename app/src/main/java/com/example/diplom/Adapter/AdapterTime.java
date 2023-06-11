package com.example.diplom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;
import com.example.diplom.model.EstimateSubTask;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.List;

public class AdapterTime extends RecyclerView.Adapter<NewViewHolder> {

    private Context context;
    private List<EstimateSubTask> estimateSubTaskList;

    public AdapterTime(Context context, List<EstimateSubTask> estimateSubTaskList) {
        this.context = context;
        this.estimateSubTaskList = estimateSubTaskList;
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_time, parent, false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewViewHolder holder, int position) {
        holder.subtaskName.setText(estimateSubTaskList.get(position).getSubtaskName());
        holder.subtaskTo.setText(estimateSubTaskList.get(position).getSubtaskTo());
        holder.subtaskFrom.setText(estimateSubTaskList.get(position).getSubtaskFrom());
    }

    @Override
    public int getItemCount() {
        return estimateSubTaskList.size();
    }
}

class NewViewHolder extends RecyclerView.ViewHolder {

    TextView subtaskName, subtaskTo, subtaskFrom;
    CardView recCard;

    public NewViewHolder(@NonNull View itemView) {
        super(itemView);
        recCard = itemView.findViewById(R.id.recCardSub);
        subtaskTo = itemView.findViewById(R.id.subtaskTo);
        subtaskFrom = itemView.findViewById(R.id.subtaskFrom);
        subtaskName = itemView.findViewById(R.id.subtaskName);
    }
}

