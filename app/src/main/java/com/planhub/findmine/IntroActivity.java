package com.planhub.findmine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapater introViewPagerAdapater;
    TabLayout tabIndicator;
    Button btnNext;
    Button btnPindah;
    int position = 0;
    Animation btnAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Buat full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Activity intro akan berjaan hanya sekali saat membuka aplikasi
        if (restorePrefData()) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }

        setContentView(R.layout.activity_intro);


        // Init view
        btnNext = findViewById(R.id.btnNext);
        btnPindah = findViewById(R.id.btnPindah);
        tabIndicator = findViewById(R.id.tabIndicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.btn_anim);


        // Isi list screen
        final List<ScreenItem> mListItemScreen = new ArrayList<>();
        mListItemScreen.add(new ScreenItem("Daftar atau Masuk", "Masuk atau daftar untuk menggunakan aplikasi ini", R.drawable.slide_01));
        mListItemScreen.add(new ScreenItem("Unggah Barang", "Unggah barang yang anda temukan", R.drawable.slide_02));
        mListItemScreen.add(new ScreenItem("Temukan Barang", "Temukan barang anda yang hilang", R.drawable.slide_03));


        // Setup viewpager
        screenPager = findViewById(R.id.screenVewPager);
        introViewPagerAdapater = new IntroViewPagerAdapater(this, mListItemScreen);
        screenPager.setAdapter(introViewPagerAdapater);


        // Setup tab Indicator
        tabIndicator.setupWithViewPager(screenPager);

        // Setup button click (geser intro)
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();

                if (position < mListItemScreen.size()) {

                    position++;
                    screenPager.setCurrentItem(position);
                }

                // Perintah button di akhir gambar intro
                if (position == mListItemScreen.size() - 1) {
                    // TODO : tampil tombol dan menyembunyikan indikator

                    loadLastScreen();
                }

            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mListItemScreen.size() - 1) {

                    loadLastScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Pindah activity ke main
        btnPindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);

                savePrefData();
                finish();

            }
        });

    }

    private boolean restorePrefData() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Prefs berjalan", MODE_PRIVATE);
        Boolean introActivity = preferences.getBoolean("Intro activity berjalan", false);
        return introActivity;

    }

    // Save preference
    private void savePrefData() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Prefs berjalan", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Intro activity berjalan", true);
        editor.commit();

    }

    // Menampilkan button
    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnPindah.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        //TODO : setup tombol animasi
        btnPindah.setAnimation(btnAnim);

    }
}
