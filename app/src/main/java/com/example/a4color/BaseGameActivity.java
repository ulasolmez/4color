package com.example.a4color;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

        LevelGenerator generator = new ProgressiveLevelGenerator();
        levelManager = new LevelManager(generator, metrics.widthPixels, metrics.heightPixels);
        levelManager.generateLevels(20); // Generate 20 levels

        int levelNumber = getIntent().getIntExtra("LEVEL_NUMBER", 1);
        levelManager.setLevel(levelNumber - 1);

        gameState = new GameState(levelManager.getCurrentLevel());

        ColorPicker colorPicker = new MaterialColorPicker();
        gameView = new GameView(this, gameState, colorPicker);
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
                        gameState.setCurrentLevel(levelManager.getCurrentLevel());
                        gameView.invalidate();
                    }
                })
                .setNegativeButton("Retry", (dialog, which) -> {
                    resetLevel();
                })
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

    public void onUndoClicked(View view) {
        gameView.invalidate();
    }
}