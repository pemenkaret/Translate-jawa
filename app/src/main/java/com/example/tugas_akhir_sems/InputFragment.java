package com.example.tugas_akhir_sems;

// Import library yang dibutuhkan
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

// Kelas InputFragment mewarisi Fragment untuk membuat UI sebagai bagian dari aplikasi
public class InputFragment extends Fragment {

    // Deklarasi variabel untuk elemen UI dan referensi ke database Firebase
    private EditText edtIndonesia, edtKramaAlus, edtKramaInggil, edtNgoko;
    private Button btnSubmit;
    private DatabaseReference databaseRef;

    // Konstruktor kosong diperlukan oleh Fragment
    public InputFragment() {
        // Diperlukan konstruktor kosong untuk Fragment
    }

    // Method ini dipanggil saat Fragment membuat UI-nya
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        databaseRef = FirebaseDatabase.getInstance().getReference("employees");
        edtIndonesia = view.findViewById(R.id.et_indonesia);
        edtKramaAlus = view.findViewById(R.id.et_kramaalus);
        edtKramaInggil = view.findViewById(R.id.et_kramainggil);
        edtNgoko = view.findViewById(R.id.et_ngoko);
        btnSubmit = view.findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(v -> {
            // Mengambil nilai input dari semua EditText
            String indonesia = edtIndonesia.getText().toString().trim();
            String kramaAlus = edtKramaAlus.getText().toString().trim();
            String kramaInggil = edtKramaInggil.getText().toString().trim();
            String ngoko = edtNgoko.getText().toString().trim();

            // Validasi input, semua kolom harus terisi
            if (!indonesia.isEmpty() && !kramaAlus.isEmpty() && !kramaInggil.isEmpty() && !ngoko.isEmpty()) {
                // Memeriksa data di database sebelum menambahkan
                checkAndInsertData(indonesia, kramaAlus, kramaInggil, ngoko);
            } else {
                Toast.makeText(requireActivity(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            }
        });

        return view; // Mengembalikan view untuk ditampilkan
    }

    // Method untuk memeriksa apakah data sudah ada sebelum ditambahkan ke database
    private void checkAndInsertData(String indonesia, String kramaAlus, String kramaInggil, String ngoko) {
        // Query Firebase untuk mencari data dengan field "indonesia" yang cocok
        databaseRef.orderByChild("indonesia").equalTo(indonesia).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Data sudah ada, tampilkan pesan
                    Toast.makeText(requireActivity(), "Data sudah ada!", Toast.LENGTH_SHORT).show();
                } else {
                    // Data belum ada, tambahkan ke database
                    insertData(indonesia, kramaAlus, kramaInggil, ngoko);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tampilkan log jika ada error saat membaca database
                Log.e("Firebase", "Gagal membaca database", error.toException());
            }
        });
    }

    // Method untuk menambahkan data baru ke Firebase Realtime Database
    private void insertData(String indonesia, String kramaAlus, String kramaInggil, String ngoko) {
        // Membuat key unik untuk setiap entri di database
        String uniqueKey = databaseRef.push().getKey();

        // Membuat map untuk menyimpan data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("indonesia", indonesia);
        dataMap.put("kramaalus", kramaAlus);
        dataMap.put("kramainggil", kramaInggil);
        dataMap.put("ngoko", ngoko);

        // Menyimpan data ke database pada key unik
        databaseRef.child(uniqueKey).setValue(dataMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireActivity(), "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Gagal menambahkan data!", Toast.LENGTH_SHORT).show();
                });
    }
}
