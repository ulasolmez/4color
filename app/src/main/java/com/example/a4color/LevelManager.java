package com.example.a4color;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LevelManager {

    private static final int[] NODE_COUNTS = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final float[] EDGE_DENSITIES = {1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f,
            1.8f, 1.9f, 2.0f, 2.1f, 2.2f, 2.3f};
    private static List<Level> levels;
    private static int currentLevelIndex = 0;
    private final int screenWidth;
    private final int screenHeight;

    public LevelManager(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        levels = new ArrayList<>();
        generateLevels();
    }
    public static int getCurrentLevelIndex() {
        return currentLevelIndex;
    }
    public static void setCurrentLevel(int levelIndex) {
        if (levelIndex >= 0 && levelIndex < levels.size()) {
            currentLevelIndex = levelIndex;
        }
    }

    private void generateLevels() {
        // Generate 20 progressively harder levels
        for (int i = 0; i < 40; i++) {
            levels.add(generateLevel(i));
        }
    }

    private Level generateLevel(int levelNumber) {
        int levelIndex = Math.min(levelNumber, NODE_COUNTS.length - 1);
        int nodeCount = NODE_COUNTS[levelIndex];
        float edgeDensity = EDGE_DENSITIES[levelIndex];

        List<Node> nodes = createCircularLayout(nodeCount);
        List<Edge> edges = createEdges(nodes, (int)(nodeCount * edgeDensity));

        return new Level(nodes, edges);
    }
    private List<Node> createCircularLayout(int nodeCount) {
        List<Node> nodes = new ArrayList<>();
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;
        float radius = Math.min(screenWidth, screenHeight) * 0.4f;

        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount;
            float x = centerX + (float)(radius * Math.cos(angle));
            float y = centerY + (float)(radius * Math.sin(angle));
            nodes.add(new Node(i, x, y));
        }
        return nodes;
    }

    private List<Edge> createEdges(List<Node> nodes, int targetEdges) {
        List<Edge> edges = new ArrayList<>();
        Random random = new Random();

        // Ensure graph is connected
        for (int i = 1; i < nodes.size(); i++) {
            edges.add(new Edge(nodes.get(i-1), nodes.get(i)));
        }

        // Add remaining edges randomly
        while (edges.size() < targetEdges) {
            Node a = nodes.get(random.nextInt(nodes.size()));
            Node b = nodes.get(random.nextInt(nodes.size()));

            if (a != b && !hasEdge(edges, a, b)) {
                edges.add(new Edge(a, b));
            }
        }
        return edges;
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