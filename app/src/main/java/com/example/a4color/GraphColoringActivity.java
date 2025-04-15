package com.example.a4color;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class GraphColoringActivity extends AppCompatActivity {
    private GameView gameView;
    private LevelManager levelManager;
    private String currentGraphType = "wheel"; // Default generator type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get screen dimensions
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Get level and graph type from intent
        int levelNumber = getIntent().getIntExtra("LEVEL_NUMBER", 1);
        String graphType = getIntent().getStringExtra("GRAPH_TYPE");
        if (graphType != null) {
            currentGraphType = graphType;
        }

        // Initialize LevelManager with selected generator
        levelManager = new LevelManager(metrics.widthPixels, metrics.heightPixels);
        levelManager.setGeneratorType(currentGraphType);
        LevelManager.setCurrentLevel(levelNumber - 1);

        gameView = new GameView(this, LevelManager.getCurrentLevel());
        setContentView(gameView);

        setupToolbar();
        showGraphTypeToast();
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Level " + (LevelManager.getCurrentLevelIndex() + 1) +
                    " (" + currentGraphType + ")");
        }
    }

    private void showGraphTypeToast() {
        String graphName = "";
        switch (currentGraphType) {
            case "progressive":
                graphName = "Progressive Graph";
                break;
            case "wheel":
                graphName = "Wheel Graph";
                break;
            // Add more cases as you add generators
            default:
                graphName = "Special Graph";
        }
        Toast.makeText(this, graphName, Toast.LENGTH_SHORT).show();
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

    // Method to change graph type dynamically
    public void changeGraphType(View view, String newType) {
        if (!currentGraphType.equals(newType)) {
            currentGraphType = newType;
            levelManager.setGeneratorType(newType);

            Level newLevel = LevelManager.getCurrentLevel();

            // Update the GameView with the new level
            gameView.setLevel(newLevel);

            setupToolbar(); // Update title
            showGraphTypeToast();
        }
    }
}