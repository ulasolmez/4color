package com.example.a4color;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.List;

public abstract class BaseGameActivity extends AppCompatActivity
        implements GameView.GameEventListener {

    protected GameView gameView;
    protected GameState gameState;
    protected LevelManager levelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGameComponents();
    }

    protected void setupGameComponents() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        LevelGenerator generator = new WheelGraphGenerator();

        levelManager = new LevelManager(generator, metrics.widthPixels, metrics.heightPixels);
        levelManager.generateLevels(20); // Generate 20 levels

        int levelNumber = getIntent().getIntExtra("LEVEL_NUMBER", 1);
        levelManager.setLevel(levelNumber - 1);

        gameState = new GameState(levelManager.getCurrentLevel());


        ColorPicker colorPicker = new MaterialColorPicker();

        gameView = new GameView(this, gameState, colorPicker);
        gameView.setGameEventListener(this);
        setContentView(gameView);

    }


    @Override
    public void onLevelCompleted() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Level Complete!")
                .setMessage("Great job coloring the graph!")
                .setPositiveButton("Next Level", (dialog, which) -> {
                    if (levelManager.hasNextLevel()) {
                        levelManager.nextLevel();
                        loadCurrentLevel();
                    } else {
                        showGameCompleted();
                    }
                })
                .setNegativeButton("Retry", (dialog, which) -> resetLevel())
                .show();
    }

    protected void loadCurrentLevel() {
        gameState = new GameState(levelManager.getCurrentLevel());
        gameView.setGameState(gameState);
        gameView.invalidate();
        updateLevelTitle();
    }

    void updateLevelTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Level " + (levelManager.getCurrentLevelIndex() + 1));
        }
    }

    private void showGameCompleted() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Congratulations!")
                .setMessage("You've completed all levels!")
                .setPositiveButton("Back to Menu", (dialog, which) -> finish())
                .show();
    }

    private void resetLevel() {
        Level current = levelManager.getCurrentLevel();
        for (Node node : current.getNodes()) {
            node.setColor(Color.WHITE);
        }
        gameState = new GameState(current);
        gameView.setGameState(gameState);
        gameView.invalidate();
    }
}