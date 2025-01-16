package com.example.tugas_akhir_sems;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingFragment extends Fragment {

    private SwitchMaterial switchNotification;
    private Button btnSaveSettings, btnPlayMusic;
    private MediaPlayer mediaPlayer; // Deklarasi MediaPlayer untuk memutar musik

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Menghubungkan layout XML dengan fragmen
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Inisialisasi komponen
        switchNotification = view.findViewById(R.id.switch_notification);
        btnSaveSettings = view.findViewById(R.id.btn_save_settings);
        btnPlayMusic = view.findViewById(R.id.btn_play_music); // Tombol untuk memutar musik

        // Inisialisasi MediaPlayer
//        mediaPlayer = MediaPlayer.create(getContext(), R.raw.lagu_jawa); // Pastikan file audio ada di folder raw

        // Muat pengaturan notifikasi yang tersimpan
        loadSettings();

        // Set listener untuk tombol simpan
        btnSaveSettings.setOnClickListener(v -> saveSettings());

        // Set listener untuk tombol putar musik
        btnPlayMusic.setOnClickListener(v -> toggleMusicPlayback());

        return view;
    }

    private void loadSettings() {
        // Mengambil pengaturan notifikasi dari SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isNotificationEnabled = preferences.getBoolean("Notifications", true); // Default ke true

        // Terapkan status notifikasi ke Switch
        switchNotification.setChecked(isNotificationEnabled);
    }

    private void saveSettings() {
        // Memeriksa status notifikasi
        boolean isNotificationEnabled = switchNotification.isChecked();

        // Simpan pengaturan ke SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Notifications", isNotificationEnabled);
        editor.apply();

        // Tampilkan pesan konfirmasi
        Toast.makeText(getContext(), "Pengaturan berhasil disimpan", Toast.LENGTH_SHORT).show();
    }

    private void toggleMusicPlayback() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();  // Jika musik sedang diputar, hentikan
            btnPlayMusic.setText("Putar Musik"); // Ubah teks tombol
        } else {
            mediaPlayer.start();  // Mulai memutar musik
            btnPlayMusic.setText("Stop Musik"); // Ubah teks tombol
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // Pastikan mediaPlayer dibebaskan ketika fragment tidak terlihat
        }
    }
}
