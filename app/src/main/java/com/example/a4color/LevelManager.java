package com.example.a4color;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelManager {


    private static List<Level> levels;
    private static int currentLevelIndex = 0;
    private int screenWidth, screenHeight;

    public LevelManager(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.levels = new ArrayList<>();
        generateLevels();
    }

    private void generateLevels() {
        // Generate 20 progressively harder levels
        for (int i = 0; i < 20; i++) {
            levels.add(generateLevel(i));
        }
    }

    private Level generateLevel(int levelNumber) {
        // Simplified progressive generation
        int baseNodes = 4 + (levelNumber / 3);
        int nodes = Math.min(baseNodes, 15); // Cap at 15 nodes
        int edges = nodes + (levelNumber*3 / 4); // Progressive edge count

        List<Node> nodeList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();

        // Circular layout for consistency
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;
        float radius = Math.min(screenWidth, screenHeight) * 0.4f;

        for (int i = 0; i < nodes; i++) {
            double angle = 2 * Math.PI * i / nodes;
            float x = centerX + (float)(radius * Math.cos(angle));
            float y = centerY + (float)(radius * Math.sin(angle));
            nodeList.add(new Node(i, x, y));
        }

        // Connect nodes progressively
        for (int i = 0; i < edges; i++) {
            int from = i % nodes;
            int to = (from + 1 + (i / nodes)) % nodes;
            if (!hasEdge(edgeList, nodeList.get(from), nodeList.get(to))) {
                edgeList.add(new Edge(nodeList.get(from), nodeList.get(to)));
            }
        }

        return new Level(nodeList, edgeList);
    }

    private boolean hasEdge(List<Edge> edges, Node a, Node b) {
        for (Edge e : edges) {
            if ((e.getStart() == a && e.getEnd() == b) ||
                    (e.getStart() == b && e.getEnd() == a)) {
                return true;
            }
        }
        return false;
    }

    // Keep your existing methods
    public static Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public static void nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
        }
    }

    public void reset() {
        currentLevelIndex = 0;
    }
   /*

    private static List<Level> levels;
    private static int currentLevel = 0;
    private int screenWidth, screenHeight;

    public LevelManager(int width, int height) {
        generateLevels(width, height);
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = Math.min(Math.max(level, 0), levels.size() - 1);
    }

    private void generateLevels(int width, int height) {
        levels = new ArrayList<>();
        // Generate increasingly complex levels
        for (int i = 0; i < 10; i++) {
            levels.add(GraphGenerator.generatePlanarGraph(width, height));
        }
    }
    public static Level getCurrentLevel() {
        return levels.get(currentLevel);
    }

    public static void nextLevel() {
        currentLevel = Math.min(currentLevel + 1, levels.size() - 1);
    }
}

    */
}