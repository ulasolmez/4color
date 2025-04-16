package com.example.a4color;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class LevelManager {
    private final List<Level> levels = new ArrayList<>();
    private int currentLevelIndex = 0;
    private final int screenWidth;
    private final int screenHeight;
    private final LevelGenerator levelGenerator;

    public LevelManager(LevelGenerator levelGenerator, int width, int height) {
        this.levelGenerator = levelGenerator;
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void generateLevels(int count) {
        levels.clear();
        for (int i = 0; i < count; i++) {
            levels.add(levelGenerator.generateLevel(i, screenWidth, screenHeight));
        }
    }

    public Level getCurrentLevel() {
        if (levels.isEmpty()) return null;
        return levels.get(currentLevelIndex);
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public boolean hasNextLevel() {
        return currentLevelIndex < levels.size() - 1;
    }

    public void nextLevel() {
        if (hasNextLevel()) {
            currentLevelIndex++;
        }
    }

    public void setLevel(int levelIndex) {
        if (levelIndex >= 0 && levelIndex < levels.size()) {
            currentLevelIndex = levelIndex;
        }
    }

    public int getLevelCount() {
        return levels.size();
    }
}