package com.example.tugas_akhir_sems;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TranslationDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView originalText, translatedText;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_detail);

        // Mendapatkan referensi Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("employees");

        // Menghubungkan elemen layout
        originalText = findViewById(R.id.originalText);
        translatedText = findViewById(R.id.translatedText);
        btnBack = findViewById(R.id.btnBack);

        // Mendapatkan data dari Intent
        String originalWord = getIntent().getStringExtra("originalWord");
        String input = originalWord; // Input yang akan dicari

        // Menampilkan kata asli
        originalText.setText("Kata Asli: " + originalWord);

        // Mencari terjemahan di Firebase
        findTranslation(input);

        // Tombol Kembali
        btnBack.setOnClickListener(v -> {
            finish(); // Menutup activity dan kembali ke fragment sebelumnya
        });
    }

    private void findTranslation(String input) {
        // Memecah input menjadi array kata
        String[] words = input.split("\\s+");
        StringBuilder translationResult = new StringBuilder();
        List<String> notFoundWords = new ArrayList<>();

        // Mencari terjemahan untuk setiap kata dalam input
        for (String word : words) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean ditemukan = false;

                    // Iterasi semua data di Firebase
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String indonesia = data.child("indonesia").getValue(String.class);
                        String kramaalus = data.child("kramaalus").getValue(String.class);
                        String kramainggil = data.child("kramainggil").getValue(String.class);
                        String ngoko = data.child("ngoko").getValue(String.class);

                        // Cek kecocokan penuh dengan input (case insensitive)
                        if (indonesia != null && indonesia.equalsIgnoreCase(word)) {
                            ditemukan = true;
                            if (ngoko != null) {
                                translationResult.append("Ngoko: ").append(ngoko).append(",");
                            }
                            if (kramainggil != null) {
                                translationResult.append("Krama Inggil: ").append(kramainggil).append(",");
                            }
                            if (kramaalus != null) {
                                translationResult.append("Krama Alus: ").append(kramaalus).append(". \n");
                            }

                            break;
                        } else if (ngoko != null && ngoko.equalsIgnoreCase(word)) {
                            ditemukan = true;
                            translationResult.append("Indonesia: ").append(indonesia).append("\n");
                            break;
                        }
                    }

                    // Jika kata tidak ditemukan, simpan untuk ditampilkan nanti
                    if (!ditemukan) {
                        notFoundWords.add(word);
                    }

                    // Jika semua kata sudah diproses, tampilkan hasilnya
                    if (translationResult.length() > 0) {
                        translatedText.setText("Hasil Terjemahan :\n" + translationResult.toString());
                    }

                    if (notFoundWords.size() > 0) {
                        translatedText.append("\nKata yang tidak ditemukan: " + notFoundWords.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Menangani kesalahan jika Firebase gagal diakses
                    translatedText.setText("Gagal mengambil data terjemahan.");
                }
            });
        }
    }
}
