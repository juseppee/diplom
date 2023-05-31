package com.example.diplom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplom.R;
import com.example.diplom.model.EstimateLine;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.List;

public class AdapterTime extends RecyclerView.Adapter<NewViewHolder> {

    private Context context;
    private List<EstimateLine> estimateLineList;

    public AdapterTime(Context context, List<EstimateLine> estimateLineList) {
        this.context = context;
        this.estimateLineList = estimateLineList;
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_time, parent, false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewViewHolder holder, int position) {
        holder.subtaskName.setText(estimateLineList.get(position).getSubtaskName());
        holder.subtaskTo.setText(estimateLineList.get(position).getSubtaskTo());
        holder.subtaskFrom.setText(estimateLineList.get(position).getSubtaskFrom());
    }

    @Override
    public int getItemCount() {
        return estimateLineList.size();
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

