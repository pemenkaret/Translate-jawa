package com.example.tugas_akhir_sems;
import com.example.tugas_akhir_sems.R;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi DrawerLayout dan NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.bagian_navigation);

        // Inisialisasi Toolbar
        Toolbar toolbar = findViewById(R.id.bagian_toolbar);
        // Jika ActionBar bawaan sudah dinonaktifkan, tidak perlu memanggil setSupportActionBar

        // Menambahkan tombol hamburger di Toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Menangani item yang dipilih di NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment = null;

            // Mengatur fragment berdasarkan item menu yang dipilih
            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_favorites) {
                fragment = new FavoriteFragment();
            } else if (id == R.id.nav_about) {
                fragment = new AboutFragment();
            } else if (id == R.id.nav_setting) {
                fragment = new SettingFragment();
            }

            if (fragment != null) {
                // Ganti fragment yang ditampilkan
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.bagian_frame, fragment);
                fragmentTransaction.commit();
            }

            // Menutup drawer setelah item dipilih
            drawerLayout.closeDrawers();
            return true;
        });

        // Menampilkan fragment pertama kali (HomeFragment)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bagian_frame, new HomeFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        // Menangani aksi tombol back dan menutup drawer jika terbuka
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
