package com.example.a4color;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

// LevelSelectActivity.java
public class LevelSelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        GridLayout levelGrid = findViewById(R.id.levelGrid);
        int totalLevels = 21; // Change this to your number of levels

        for (int i = 1; i <= totalLevels; i++) {
            Button levelButton = new Button(this);
            levelButton.setText(String.valueOf(i));
            levelButton.setTextSize(18);
            levelButton.setPadding(20, 50, 20, 50);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(16, 16, 16, 16);

            levelButton.setLayoutParams(params);

            final int levelNumber = i;
            levelButton.setOnClickListener(v -> {
                Intent intent = new Intent(LevelSelectActivity.this, GraphColoringActivity.class);
                intent.putExtra("LEVEL_NUMBER", levelNumber);
                startActivity(intent);
            });

            levelGrid.addView(levelButton);
        }
    }
}