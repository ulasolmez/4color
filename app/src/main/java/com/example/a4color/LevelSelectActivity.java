package com.example.a4color;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

// LevelSelectActivity.java
public class LevelSelectActivity extends AppCompatActivity {
    private static final int TOTAL_LEVELS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        GridLayout levelGrid = findViewById(R.id.levelGrid);
        levelGrid.setColumnCount(5); // 5 columns

        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            Button levelButton = new Button(this);
            final int levelNumber = i; // Create final copy for lambda


            // Style and text (unchanged)
            styleLevelButton(levelButton, levelNumber);
            levelButton.setText(String.valueOf(levelNumber));

            // Use the final copy in the lambda
            levelButton.setOnClickListener(v -> startLevel(levelNumber));

            // Layout params (unchanged)
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = dpToPx(60);
            params.height = dpToPx(60);
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

            levelGrid.addView(levelButton, params);
        }
    }

    private void startLevel(int levelNumber) {
        startActivity(new Intent(this, GraphColoringActivity.class)
                .putExtra("LEVEL_NUMBER", levelNumber));
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void styleLevelButton(Button button, int level) {
        // Implement your styling logic here
        // For example, change color based on completion status
    }
}