package com.example.tugas_akhir_sems;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoriteFragment extends Fragment {

    private ListView listViewJawaToIndonesia;
    private ListView listViewIndonesiaToJawa;
    private ArrayList<String> favoriteJawaToIndonesia = new ArrayList<>();
    private ArrayList<String> favoriteIndonesiaToJawa = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout fragment dan temukan elemen ListView
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        listViewJawaToIndonesia = view.findViewById(R.id.listViewJawaToIndonesia);
        listViewIndonesiaToJawa = view.findViewById(R.id.listViewIndonesiaToJawa);

        // Memuat kata favorit dari penyimpanan lokal
        loadFavoriteWords();

        // Adapter untuk daftar Jawa ke Indonesia
        ArrayAdapter<String> adapterJawaToIndonesia = new ArrayAdapter<String>(
                getContext(), R.layout.list_item_favorite, R.id.favoriteWordText, favoriteJawaToIndonesia) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                // Mendapatkan atau membuat ulang tampilan item daftar
//                View view = super.getView(position, convertView, parent);
//
//                // Mengatur teks utama dari daftar kata favorit
//                TextView favoriteWordText = view.findViewById(R.id.favoriteWordText);
//                String word = favoriteJawaToIndonesia.get(position);
//                favoriteWordText.setText(word);
//
//////                favoriteWordDetailText.setText("Diterjemahkan dari Jawa ke Indonesia");
//
//                // Tombol hapus untuk menghapus kata dari daftar favorit
//                Button btnDelete = view.findViewById(R.id.btnDelete);
//                btnDelete.setOnClickListener(v -> {
//                    String wordToDelete = favoriteJawaToIndonesia.get(position);
//                    deleteFavoriteWord(wordToDelete, true); // Hapus dari penyimpanan lokal
//                    favoriteJawaToIndonesia.remove(position); // Hapus dari memori
//                    notifyDataSetChanged(); // Perbarui tampilan daftar
//                });
//
//                return view;
//            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Mengatur teks utama
                TextView favoriteWordText = view.findViewById(R.id.favoriteWordText);
                String word = favoriteJawaToIndonesia.get(position);
                favoriteWordText.setText(word);

                // Menambahkan klik pada seluruh item
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), TranslationDetailActivity.class);
                    intent.putExtra("originalWord", word);
                    intent.putExtra("translatedWord", "Terjemahan dari Jawa ke Indonesia"); // Ganti sesuai logika terjemahan
                    getContext().startActivity(intent);
                });

                // Tombol hapus
                Button btnDelete = view.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(v -> {
                    String wordToDelete = favoriteJawaToIndonesia.get(position);
                    deleteFavoriteWord(wordToDelete, true);
                    favoriteJawaToIndonesia.remove(position);
                    notifyDataSetChanged();
                });

                return view;
            }

        };
        listViewJawaToIndonesia.setAdapter(adapterJawaToIndonesia); // Tetapkan adapter ke ListView

        // Adapter untuk daftar Indonesia ke Jawa
        ArrayAdapter<String> adapterIndonesiaToJawa = new ArrayAdapter<String>(
                getContext(), R.layout.list_item_favorite, R.id.favoriteWordText, favoriteIndonesiaToJawa) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                // Mendapatkan atau membuat ulang tampilan item daftar
//                View view = super.getView(position, convertView, parent);
//
//                // Mengatur teks utama dari daftar kata favorit
//                TextView favoriteWordText = view.findViewById(R.id.favoriteWordText);
//                String word = favoriteIndonesiaToJawa.get(position);
//                favoriteWordText.setText(word);
//
//////                favoriteWordDetailText.setText("Diterjemahkan dari Indonesia ke Jawa");
//
//                // Tombol hapus untuk menghapus kata dari daftar favorit
//                Button btnDelete = view.findViewById(R.id.btnDelete);
//                btnDelete.setOnClickListener(v -> {
//                    String wordToDelete = favoriteIndonesiaToJawa.get(position);
//                    deleteFavoriteWord(wordToDelete, false); // Hapus dari penyimpanan lokal
//                    favoriteIndonesiaToJawa.remove(position); // Hapus dari memori
//                    notifyDataSetChanged(); // Perbarui tampilan daftar
//                });
//
//                return view;
//            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Mengatur teks utama
                TextView favoriteWordText = view.findViewById(R.id.favoriteWordText);
                String word = favoriteIndonesiaToJawa.get(position);
                favoriteWordText.setText(word);

                // Menambahkan klik pada seluruh item
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), TranslationDetailActivity.class);
                    intent.putExtra("originalWord", word);
                    intent.putExtra("translatedWord", "Terjemahan dari Indonesia ke Jawa"); // Ganti sesuai logika terjemahan
                    getContext().startActivity(intent);
                });

                // Tombol hapus
                Button btnDelete = view.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(v -> {
                    String wordToDelete = favoriteIndonesiaToJawa.get(position);
                    deleteFavoriteWord(wordToDelete, false);
                    favoriteIndonesiaToJawa.remove(position);
                    notifyDataSetChanged();
                });

                return view;
            }
        };
        listViewIndonesiaToJawa.setAdapter(adapterIndonesiaToJawa); // Tetapkan adapter ke ListView

        return view;
    }


    private void loadFavoriteWords() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FavoriteWords", Context.MODE_PRIVATE);

        Set<String> favoriteJawa = sharedPreferences.getStringSet("favorite_jawa", new HashSet<>());
        Set<String> favoriteIndonesia = sharedPreferences.getStringSet("favorite_indonesia", new HashSet<>());

        favoriteJawaToIndonesia.clear();
        favoriteIndonesiaToJawa.clear();

        favoriteJawaToIndonesia.addAll(favoriteJawa);
        favoriteIndonesiaToJawa.addAll(favoriteIndonesia);
    }

    private void deleteFavoriteWord(String word, boolean isJawaToIndonesia) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FavoriteWords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> favoriteWords = new HashSet<>(sharedPreferences.getStringSet(isJawaToIndonesia ? "favorite_jawa" : "favorite_indonesia", new HashSet<>()));

        if (favoriteWords.contains(word)) {
            favoriteWords.remove(word);
            if (isJawaToIndonesia) {
                editor.putStringSet("favorite_jawa", favoriteWords);
            } else {
                editor.putStringSet("favorite_indonesia", favoriteWords);
            }
            editor.apply();
            Toast.makeText(getContext(), "Kata dihapus dari favorit", Toast.LENGTH_SHORT).show();
        }
    }
}
