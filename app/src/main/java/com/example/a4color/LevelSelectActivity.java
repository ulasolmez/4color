package com.example.a4color;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// LevelSelectActivity.java
public class LevelSelectActivity extends AppCompatActivity {
    private static final int TOTAL_LEVELS = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        GridLayout levelGrid = findViewById(R.id.levelGrid);
        levelGrid.setColumnCount(5);

        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            Button levelButton = new Button(this);
            final int levelNumber = i;

            int[] buttonColors = {
                    R.color.level_button_red,
                    R.color.level_button_green,
                    R.color.level_button_blue,
                    R.color.level_button_yellow
            };
            int colorResId = buttonColors[i % 4];

            GradientDrawable bgDrawable = new GradientDrawable();
            bgDrawable.setShape(GradientDrawable.RECTANGLE);
            bgDrawable.setCornerRadius(dpToPx(8));
            bgDrawable.setColor(ContextCompat.getColor(this, colorResId));

            RippleDrawable rippleDrawable = new RippleDrawable(
                    ColorStateList.valueOf(Color.parseColor("#80FFFFFF")), // Ripple color
                    bgDrawable,
                    null
            );


            levelButton.setBackground(rippleDrawable);
            levelButton.setTextColor(i % 4 == 2 ? // Blue background needs white text
                    ContextCompat.getColor(this, R.color.text_primary_light) :
                    ContextCompat.getColor(this, R.color.text_primary_dark));

            levelButton.setText(String.valueOf(i));
            levelButton.setTextSize(16);
            levelButton.setAllCaps(false);
            levelButton.setElevation(dpToPx(2));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = dpToPx(60);
            params.height = dpToPx(60);
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

            levelGrid.addView(levelButton, params);

            styleLevelButton(levelButton, levelNumber);
            levelButton.setText(String.valueOf(levelNumber));

            levelButton.setOnClickListener(v -> startLevel(levelNumber));


        }
    }

    private void startLevel(int levelNumber) {
        Log.d("LevelSelect", "Starting level " + levelNumber);

        Intent intent = new Intent(this, GraphColoringActivity.class);
        intent.putExtra("LEVEL_NUMBER", levelNumber);

        // Add flags to ensure clean activity launch
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void styleLevelButton(Button button, int level) {
        // Implement your styling logic here
        // For example, change color based on completion status
    }
}