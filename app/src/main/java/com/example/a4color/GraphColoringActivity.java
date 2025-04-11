package com.example.a4color;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
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

        // Get screen dimensions
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Initialize with proper level
        int levelNumber = getIntent().getIntExtra("LEVEL_NUMBER", 1);
        levelManager = new LevelManager(metrics.widthPixels, metrics.heightPixels);
        LevelManager.setCurrentLevel(levelNumber - 1);

        gameView = new GameView(this, LevelManager.getCurrentLevel());
        setContentView(gameView);

        setupToolbar();
    }
    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Level " + (LevelManager.getCurrentLevelIndex() + 1));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onUndoClicked(View view) {
        gameView.undoLastColor();
    }
}