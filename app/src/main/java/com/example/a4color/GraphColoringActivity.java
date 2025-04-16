package com.example.a4color;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class GraphColoringActivity extends BaseGameActivity implements GameView.GameEventListener {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        gameView.setGameEventListener(this); // Set the listener
        setContentView(gameView);

        setupToolbar();
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Level " + (levelManager.getCurrentLevelIndex() + 1));
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

    @Override
    public void onLevelCompleted() {
        // Handle level completion here
        new MaterialAlertDialogBuilder(this)
                .setTitle("Level Complete!")
                .setMessage("Great job coloring the graph!")
                .setPositiveButton("Next Level", (dialog, which) -> {
                    // Handle next level
                })
                .setNegativeButton("Retry", (dialog, which) -> {
                    // Handle retry
                })
                .show();
    }
}