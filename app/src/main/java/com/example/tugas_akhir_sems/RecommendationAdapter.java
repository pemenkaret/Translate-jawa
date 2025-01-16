package com.example.tugas_akhir_sems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    private List<String> recommendationList;

    public RecommendationAdapter(List<String> recommendationList) {
        this.recommendationList = recommendationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflasi layout item untuk setiap item rekomendasi
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommendation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String recommendation = recommendationList.get(position);
        holder.tvRecommendation.setText(recommendation);
    }

    @Override
    public int getItemCount() {
        return recommendationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecommendation;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRecommendation = itemView.findViewById(R.id.tv_recommendation);
        }
    }
}
