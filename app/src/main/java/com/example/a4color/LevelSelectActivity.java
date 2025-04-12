package com.example.a4color;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
            // 1. Assign colors from our 4-color palette
            int[] buttonColors = {
                    R.color.level_button_red,
                    R.color.level_button_green,
                    R.color.level_button_blue,
                    R.color.level_button_yellow
            };
            int colorResId = buttonColors[i % 4];

            // 2. Create a background drawable with rounded corners
            GradientDrawable bgDrawable = new GradientDrawable();
            bgDrawable.setShape(GradientDrawable.RECTANGLE);
            bgDrawable.setCornerRadius(dpToPx(8)); // 8dp rounded corners
            bgDrawable.setColor(ContextCompat.getColor(this, colorResId));

            // 3. Combine with ripple effect
            RippleDrawable rippleDrawable = new RippleDrawable(
                    ColorStateList.valueOf(Color.parseColor("#80FFFFFF")), // Ripple color
                    bgDrawable, // Background
                    null // No mask
            );

            // 4. Apply the styling
            levelButton.setBackground(rippleDrawable);
            levelButton.setTextColor(i % 4 == 2 ? // Blue background needs white text
                    ContextCompat.getColor(this, R.color.text_primary_light) :
                    ContextCompat.getColor(this, R.color.text_primary_dark));

            levelButton.setText(String.valueOf(i));
            levelButton.setTextSize(16);
            levelButton.setAllCaps(false);
            levelButton.setElevation(dpToPx(2));

            // 5. Layout params (unchanged)
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = dpToPx(60);
            params.height = dpToPx(60);
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

            levelGrid.addView(levelButton, params);

            // Style and text (unchanged)
            styleLevelButton(levelButton, levelNumber);
            levelButton.setText(String.valueOf(levelNumber));

            // Use the final copy in the lambda
            levelButton.setOnClickListener(v -> startLevel(levelNumber));


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