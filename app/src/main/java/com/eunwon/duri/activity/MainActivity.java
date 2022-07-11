package com.eunwon.duri.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.eunwon.duri.SpManager;
import com.eunwon.duri.fragment.MyFragment;
import com.eunwon.duri.R;
import com.eunwon.duri.fragment.WordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomView;

    private WordFragment wordFragment;
    private MyFragment myFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogIn();
        setUpUI();
        action();
    }

    void checkLogIn() {
        if (!SpManager.getInstance().checkIsLogIn(this)) {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void setUpUI() {
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        bottomView = findViewById(R.id.bottom_navigation);

        wordFragment = new WordFragment();
        myFragment = new MyFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, wordFragment).commitAllowingStateLoss();
    }

    void action() {
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_1) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, wordFragment).commitAllowingStateLoss();
                } else {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, myFragment).commitAllowingStateLoss();
                }

                return true;
            }
        });
    }
}