package com.example.tugas_akhir_sems;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment {

    private EditText etInputText;
    private Button btnTranslate;
    private ImageView btnSWITCH, btnarrow, btnFavorite;
    private TextView translate_hasil,rekomendasilabel;
    private RecyclerView rvRecommendations;

//    private FavoriteAdapter indonesiaAdapter, jawaAdapter;
    private DatabaseReference databaseReference;
//    private List<String> favoriteIndonesiaList, favoriteJawaList;
//    private DatabaseReference favoritesReference;
    private List<String> recommendationList;
    private RecommendationAdapter adapter;
    private boolean isJawaToIndonesia = true; //jawa ke indo
//    private String currentUserId = "userId123"; // Ganti dengan ID pengguna yang sedang aktif
        public enum TranslationDirection {
            JAWA_TO_INDONESIA,
            INDONESIA_TO_JAWA
        }
        private TranslationDirection currentDirection = TranslationDirection.INDONESIA_TO_JAWA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        etInputText = rootView.findViewById(R.id.et_input_text);
        btnTranslate = rootView.findViewById(R.id.btn_translate);
        btnFavorite = rootView.findViewById(R.id.btn_favorite);
        translate_hasil = rootView.findViewById(R.id.hasil_terjemahan);
        rvRecommendations = rootView.findViewById(R.id.rv_recommendations);
        btnarrow = rootView.findViewById(R.id.ic_arrow);
        btnSWITCH = rootView.findViewById(R.id.btn_switch_language);
        rekomendasilabel = rootView.findViewById(R.id.tv_recommendation_label);
        //arraylist fav
//        favoriteIndonesiaList = new ArrayList<>();
//        favoriteJawaList = new ArrayList<>();
//
//        indonesiaAdapter = new FavoriteAdapter(favoriteIndonesiaList);
//        jawaAdapter = new FavoriteAdapter(favoriteJawaList);

        databaseReference = FirebaseDatabase.getInstance().getReference("employees");
//        favoritesReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("favorites");


        // Inisialisasi daftar rekomendasi
        recommendationList = new ArrayList<>();
        adapter = new RecommendationAdapter(recommendationList);
        rvRecommendations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecommendations.setAdapter(adapter);

        etInputText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etInputText.setText("");
            }
        });

        btnTranslate.setOnClickListener(v -> {
            String input = etInputText.getText().toString().trim();
            if (!input.isEmpty()) {
                // Tentukan nilai boolean berdasarkan arah terjemahan
                boolean isJawaToIndonesia = (currentDirection == TranslationDirection.JAWA_TO_INDONESIA);

                // Gunakan nilai boolean untuk logging
                Log.d("InputFavorite", "Added word: " + input + ", Is Jawa: " + isJawaToIndonesia);

                // Lakukan terjemahan sesuai arah
                if (isJawaToIndonesia) {
                    translateFromJawaToIndonesia(input); // Jawa ke Indonesia
                } else {
                    translateWord(input); // Indonesia ke Jawa
                }

                // Tampilkan hasil terjemahan
                translate_hasil.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Input tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            }
        });


// Tombol Translate
//        btnTranslate.setOnClickListener(v -> {
//            String input = etInputText.getText().toString().trim();
//            if (!input.isEmpty()) {
//                // Menggunakan switch berdasarkan arah terjemahan
//                switch (currentDirection) {
//                    case JAWA_TO_INDONESIA:
//                        translateFromJawaToIndonesia(input);  // Jawa ke Indonesia
//                        break;
//                    case INDONESIA_TO_JAWA:
//                        translateWord(input);  // Indonesia ke Jawa
//                        break;
//                }
//                translate_hasil.setVisibility(View.VISIBLE);
//            } else {
//                Toast.makeText(getContext(), "Input tidak boleh kosong!", Toast.LENGTH_SHORT).show();
//            }
//        });

// Tombol Switch
        btnSWITCH.setOnClickListener(view -> {
            // Periksa apakah ini pertama kali atau tidak
            Boolean isRotated = (Boolean) btnarrow.getTag();
            if (isRotated == null) {
                isRotated = false;
                btnarrow.setRotation(180);  //kanan ke kiri
            }
            //rotasi 180 derajat
            RotateAnimation rotateAnimation = new RotateAnimation(
                    isRotated ? 0f : 180f,
                    isRotated ? 180f : 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            rotateAnimation.setDuration(300);
            rotateAnimation.setFillAfter(true);
            btnarrow.startAnimation(rotateAnimation);
            btnarrow.setTag(!isRotated);

            // Ubah arah terjemahan
            if (currentDirection == TranslationDirection.JAWA_TO_INDONESIA) {
                currentDirection = TranslationDirection.INDONESIA_TO_JAWA;
                etInputText.setHint("Masukkan teks INDONESIA");
            } else {
                currentDirection = TranslationDirection.JAWA_TO_INDONESIA;
                etInputText.setHint("Masukkan teks JAWA");
            }
// Bersihkan teks hanya saat mendapatkan fokus
            etInputText.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    etInputText.setText("");
                }
            });

        });

//        btnFavorite.setOnClickListener(v -> {
//            // Ambil teks dari EditText sebagai input
//            String inputText = etInputText.getText().toString().trim();
//            String translatedWord = translate_hasil.getText().toString();
//
//            // Pastikan input teks tidak kosong dan hasil terjemahan tidak berisi "Tidak ditemukan"
//            if (!inputText.isEmpty() && !translatedWord.contains("Tidak ditemukan")) {
//                // Menyimpan kalimat input ke dalam favorit
//                addWordToFavorites(inputTextx);
//
//                // Memberikan umpan balik kepada pengguna
//                Toast.makeText(getContext(), "Kalimat ditambahkan ke Favorit!", Toast.LENGTH_SHORT).show();
//            } else {
//                // Jika input kosong atau terjemahan tidak ditemukan
//                Toast.makeText(getContext(), "Input kosong atau hasil terjemahan tidak ditemukan", Toast.LENGTH_SHORT).show();
//            }
//        });



        btnFavorite.setOnClickListener(v -> {
            String input = etInputText.getText().toString().trim();
            if (!input.isEmpty()) {
                // Perbarui nilai isJawaToIndonesia berdasarkan currentDirection
                boolean isJawaToIndonesia = (currentDirection == TranslationDirection.JAWA_TO_INDONESIA);

                addWordToFavorites(input, isJawaToIndonesia);
                etInputText.setText("");
                Log.d("InputFavorite", "Added word to favorite: " + input + ", Is Jawa: " + isJawaToIndonesia);
            } else {
                Toast.makeText(getContext(), "Input tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
    private void translateWord(String input) {
        if (input.isEmpty()) {
            translate_hasil.setVisibility(View.GONE);
            rvRecommendations.setVisibility(View.GONE);
            return;
        }

        // Memeriksa apakah input adalah satu kata atau kalimat
        String[] words = input.split("\\s+");
        if (isJawaToIndonesia) {
            // Jawa ke Indonesia
            if (words.length == 1) {
                translateSingleWord(input);
            } else {
                translateSentence(words);
            }
        } else {
            //jawa ke indo
           translateFromJawaToIndonesia(input);
        }
    }

    //translate indo ke jawa berupa kata
    private void translateSingleWord(String input) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                recommendationList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String indonesia = data.child("indonesia").getValue(String.class);
                    String kramaalus = data.child("kramaalus").getValue(String.class);
                    String kramainggil = data.child("kramainggil").getValue(String.class);
                    String ngoko = data.child("ngoko").getValue(String.class);

                    // Cek kecocokan penuh
                    if (indonesia != null && indonesia.equalsIgnoreCase(input)) {
                        String result = "Krama Alus: " + kramaalus + "\n" +
                                "Krama Inggil: " + kramainggil + "\n" +
                                "Ngoko: " + ngoko;
                        translate_hasil.setText("Hasil Terjemahan:\n" + result);
                        translate_hasil.setVisibility(View.VISIBLE);
                        found = true;
                    }

                    // Tambahkan rekomendasi jika ada kecocokan sebagian
                    else if (indonesia != null && indonesia.toLowerCase().contains(input.toLowerCase())) {
                        String recommendation = indonesia + " -> Krama Alus: " + kramaalus +
                                ", Krama Inggil: " + kramainggil + ", Ngoko: " + ngoko;
                        recommendationList.add(recommendation);
                    }
                }

                // Jika tidak ditemukan hasil yang cocok
                if (!found) {
                    translate_hasil.setText("Hasil Terjemahan: Tidak ditemukan");
                    translate_hasil.setVisibility(View.VISIBLE);
                }

                if (!recommendationList.isEmpty()) {
                    rekomendasilabel.setVisibility(View.VISIBLE);
                    rvRecommendations.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    rvRecommendations.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //translate indo ke jawa berupa kalimat
    private void translateSentence(String[] words) {
        if (words.length == 0) {
            translate_hasil.setVisibility(View.GONE);
            rvRecommendations.setVisibility(View.GONE);
            return;
        }

        StringBuilder ngokoSentence = new StringBuilder();
        StringBuilder kramaInggilSentence = new StringBuilder();
        StringBuilder kramaAlusSentence = new StringBuilder();
        recommendationList.clear();
        AtomicInteger wordsTranslated = new AtomicInteger(0); // Untuk menghitung kata yang diterjemahkan

        for (int i = 0; i < words.length; i++) {
            final String word = words[i];

            // Mencari terjemahan untuk setiap kata
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean wordFound = false;

                    for (DataSnapshot data : snapshot.getChildren()) {
                        String indonesia = data.child("indonesia").getValue(String.class);
                        String kramaalus = data.child("kramaalus").getValue(String.class);
                        String ngoko = data.child("ngoko").getValue(String.class);
                        String kramainggil = data.child("kramainggil").getValue(String.class);

                        if (indonesia != null && indonesia.equalsIgnoreCase(word)) {
                            // Tambahkan terjemahan untuk masing-masing bahasa
                            if (ngoko != null) {
                                ngokoSentence.append(ngoko).append(" ");
                            }
                            if (kramainggil != null) {
                                kramaInggilSentence.append(kramainggil).append(" ");
                            }
                            if (kramaalus != null) {
                                kramaAlusSentence.append(kramaalus).append(" ");
                            }

                            wordFound = true;
                            break;
                        }
                    }
                    if (!wordFound) {
                        ngokoSentence.append(word).append(" ");
                        kramaInggilSentence.append(word).append(" ");
                        kramaAlusSentence.append(word).append(" ");
                    }

                    if (wordsTranslated.incrementAndGet() == words.length) {
                        translate_hasil.setVisibility(View.VISIBLE);

                        String result = ("Ngoko: " + ngokoSentence.toString() + "\n"
                                + "Krama Inggil: " + kramaInggilSentence.toString() + "\n"
                                + "Krama Alus: " + kramaAlusSentence.toString());

                        translate_hasil.setText("Hasil Terjemahan:\n" + result);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void translateFromJawaToIndonesia(String input) {
        if (input.isEmpty()) {
            translate_hasil.setVisibility(View.GONE);
            rvRecommendations.setVisibility(View.GONE);
            return;
        }
        String[] words = input.split("\\s+");

        if (words.length == 1) {
            translateSingleWordJawaToIndonesia(input);
        } else {
            // Jika input lebih dari satu kata (kalimat), terjemahkan kalimat
            translateSentenceJawaToIndonesia(words);
        }
    }

    private void translateSingleWordJawaToIndonesia(String input) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                recommendationList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String indonesia = data.child("indonesia").getValue(String.class);
                    String kramaalus = data.child("kramaalus").getValue(String.class);
                    String kramainggil = data.child("kramainggil").getValue(String.class);
                    String ngoko = data.child("ngoko").getValue(String.class);

                    if (ngoko != null && ngoko.equalsIgnoreCase(input)) {
                        String result = "Indonesia: " + indonesia ;
                        translate_hasil.setText("Hasil Terjemahan:\n" + result);
                        translate_hasil.setVisibility(View.VISIBLE);
                        found = true;
                        break;
                    }

                    if (ngoko != null && ngoko.toLowerCase().contains(input.toLowerCase())) {
                        String recommendation = ngoko + " -> Indonesia: " + indonesia +
                                ", Krama Alus: " + kramaalus + ", Krama Inggil: " + kramainggil;
                        recommendationList.add(recommendation);
                    }
                }

                if (!found) {
                    translate_hasil.setText("Hasil Terjemahan: Tidak ditemukan");
                    translate_hasil.setVisibility(View.VISIBLE);
                }

                if (!recommendationList.isEmpty()) {
                    rekomendasilabel.setVisibility(View.VISIBLE);
                    rvRecommendations.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    rvRecommendations.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void translateSentenceJawaToIndonesia(String[] words) {
        if (words.length == 0) {
            translate_hasil.setVisibility(View.GONE);
            rvRecommendations.setVisibility(View.GONE);
            return;
        }

        StringBuilder indonesiaSentence = new StringBuilder();
        StringBuilder kramaInggilSentence = new StringBuilder();
        StringBuilder kramaAlusSentence = new StringBuilder();
        recommendationList.clear();
        AtomicInteger wordsTranslated = new AtomicInteger(0);
        // Inisialisasi untuk setiap jenis terjemahan
        for (int i = 0; i < words.length; i++) {
            final String word = words[i];
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean wordFound = false;

                    for (DataSnapshot data : snapshot.getChildren()) {
                        String indonesia = data.child("indonesia").getValue(String.class);
                        String kramaAlus = data.child("kramaalus").getValue(String.class);
                        String ngoko = data.child("ngoko").getValue(String.class);
                        String kramaInggil = data.child("kramainggil").getValue(String.class);

                        if (ngoko != null && ngoko.equalsIgnoreCase(word)) {
                            if (indonesia != null) {
                                indonesiaSentence.append(indonesia).append(" ");
                            }
                            if (kramaInggil != null) {
                                kramaInggilSentence.append(kramaInggil).append(" ");
                            }
                            if (kramaAlus != null) {
                                kramaAlusSentence.append(kramaAlus).append(" ");
                            }

                            wordFound = true;
                            break;
                        }
                    }

                    // Jika tidak ditemukan, biarkan kata asli
                    if (!wordFound) {
                        indonesiaSentence.append(word).append(" ");
                        kramaInggilSentence.append(word).append(" ");
                        kramaAlusSentence.append(word).append(" ");
                    }

                    // Jika semua kata sudah diterjemahkan, tampilkan hasilnya
                    if (wordsTranslated.incrementAndGet() == words.length) {
                        translate_hasil.setVisibility(View.VISIBLE);

                        String result = ("Indonesia: " + indonesiaSentence.toString());

                        translate_hasil.setText("Hasil Terjemahan:\n" + result);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addWordToFavorites(String word, boolean isJawa) {
        // Akses SharedPreferences untuk menyimpan data lokal
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FavoriteWords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Ambil daftar kata favorit berdasarkan bahasa (Jawa atau Indonesia)
        Set<String> favoriteWords = new HashSet<>(sharedPreferences.getStringSet(
                isJawa ? "favorite_jawa" : "favorite_indonesia", new HashSet<>()
        ));

        //tambahkan kata jika belum ada di daftar favorit
        if (!favoriteWords.contains(word)) {
            favoriteWords.add(word);
            editor.putStringSet(isJawa ? "favorite_jawa" : "favorite_indonesia", favoriteWords);
            editor.apply();

            Toast.makeText(getContext(), "Kata Ditambahkan ke Favorit!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Kata Sudah Ada di Favorit!", Toast.LENGTH_SHORT).show();
        }
    }

}
