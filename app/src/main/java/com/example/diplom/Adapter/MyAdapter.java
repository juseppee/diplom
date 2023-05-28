package com.example.diplom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diplom.DetailActivity;
import com.example.diplom.model.Estimate;
import com.example.diplom.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Estimate> estimateList;
    public MyAdapter(Context context, List<Estimate> estimateList) {
        this.context = context;
        this.estimateList = estimateList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("1222НУ МОЖЕТ ХОТЬ ЭТО РАБОТАЕТ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("НА СВЯЗИ ОТЛАДКА ИЗ АДАПТЕРА_МОЙ_МОЙЙЙ " + estimateList.get(position).getEstimateName());
        Glide.with(context).load(estimateList.get(position).getEstimateImage()).into(holder.recImage);
        holder.recTitle.setText(estimateList.get(position).getEstimateName());
        holder.recDesc.setText(estimateList.get(position).getEstimateDesc());
        holder.recAddInfo.setText(estimateList.get(position).getEstimateAddInfo());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", estimateList.get(holder.getAbsoluteAdapterPosition()).getEstimateImage());
                intent.putExtra("Description", estimateList.get(holder.getAbsoluteAdapterPosition()).getEstimateDesc());
                intent.putExtra("Name", estimateList.get(holder.getAbsoluteAdapterPosition()).getEstimateName());
                intent.putExtra("Additional Info", estimateList.get(holder.getAbsoluteAdapterPosition()).getEstimateAddInfo());
                intent.putExtra("Key", estimateList.get(holder.getAbsoluteAdapterPosition()).getKey());
                intent.putExtra("UserId",  estimateList.get(holder.getAbsoluteAdapterPosition()).getUserId());
                intent.putExtra("FormattedDate",  estimateList.get(holder.getAbsoluteAdapterPosition()).getFormattedDate());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return estimateList.size();
    }

    public void searchEstimateList(ArrayList<Estimate> searchList) {
        estimateList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitle, recDesc, recAddInfo;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recAddInfo = itemView.findViewById(R.id.subtaskFrom);
        recTitle = itemView.findViewById(R.id.subtaskName);
    }
}
