package com.example.a4color;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

// StartActivity.java
public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button startButton = findViewById(R.id.startButton);
        Button howToPlayButton = findViewById(R.id.howToPlayButton);

        startButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LevelSelectActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        howToPlayButton.setOnClickListener(v -> showHowToPlayDialog());
    }

    private void showHowToPlayDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("How to Play")
                .setMessage("Color all regions so that no two adjacent regions have the same color. Use only four colors!")
                .setPositiveButton("OK", null)
                .show();
    }
}
