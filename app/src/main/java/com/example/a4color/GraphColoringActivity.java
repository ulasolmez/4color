package com.example.a4color;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Main Activity
public class GraphColoringActivity extends AppCompatActivity {
    private GameView gameView;
    private LevelManager levelManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        levelManager = new LevelManager(metrics.widthPixels, metrics.heightPixels);
        gameView = new GameView(this, levelManager.getCurrentLevel());
        setContentView(gameView);
        /* super.onCreate(savedInstanceState);

        // Get level number from intent
        int levelNumber = getIntent().getIntExtra("LEVEL_NUMBER", 1);

        // Initialize with screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        levelManager = new LevelManager(width, height);
        levelManager.setCurrentLevel(levelNumber - 1); // Convert to 0-based index

        gameView = new GameView(this, levelManager.getCurrentLevel());
        setContentView(gameView);

        */
    }

    public void onUndoClicked(View view) {
        gameView.undoLastColor();
    }
}