package com.example.tugas_akhir_sems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Kelas adapter untuk mengelola dan menampilkan daftar rekomendasi dalam RecyclerView
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    // List yang menyimpan data rekomendasi
    private List<String> recommendationList;

    // Constructor untuk inisialisasi data rekomendasi
    public RecommendationAdapter(List<String> recommendationList) {
        this.recommendationList = recommendationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menggunakan layout `item_recommendation` untuk item RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommendation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Mengambil data rekomendasi berdasarkan posisi
        String recommendation = recommendationList.get(position);
        // Menampilkan teks rekomendasi pada TextView
        holder.tvRecommendation.setText(recommendation);
    }

    @Override
    public int getItemCount() {
        // Mengembalikan jumlah item dalam daftar rekomendasi
        return recommendationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Deklarasi TextView untuk menampilkan rekomendasi
        TextView tvRecommendation;

        public ViewHolder(View itemView) {
            super(itemView);
            // Menghubungkan TextView dengan elemen pada layout item_recommendation
            tvRecommendation = itemView.findViewById(R.id.tv_recommendation);
        }
    }
}
