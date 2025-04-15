package com.example.a4color;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

public class NestedTrianglesGenerator implements LevelGenerator {
    private static final int BASE_LAYERS = 2; // Minimum 2 layers (outer + inner)
    private static final float SIZE_RATIO = 0.6f; // Each inner triangle is 60% size of outer
    private static final int NODES_PER_TRIANGLE = 3;

    @Override
    public Level generateLevel(int levelNumber, int width, int height) {
        int layers = BASE_LAYERS + (levelNumber / 3); // Scale layers with level
        layers = Math.min(layers, 5); // Max 5 layers

        List<Node> nodes = createNestedNodes(width, height, layers);
        List<Edge> edges = connectTriangles(nodes, layers);

        return new Level(nodes, edges);
    }

    private List<Node> createNestedNodes(int width, int height, int layers) {
        List<Node> nodes = new ArrayList<>();
        float centerX = width / 2f;
        float centerY = height / 2f;
        float baseSize = Math.min(width, height) * 0.45f;

        for (int layer = 0; layer < layers; layer++) {
            float size = baseSize * (float)Math.pow(SIZE_RATIO, layer);

            // Equilateral triangle points
            PointF top = new PointF(centerX, centerY - size);
            PointF left = new PointF(centerX - size * (float)Math.cos(Math.PI/6),
                    centerY + size * 0.5f);
            PointF right = new PointF(centerX + size * (float)Math.cos(Math.PI/6),
                    centerY + size * 0.5f);

            nodes.add(new Node(layer * 3, top.x, top.y));
            nodes.add(new Node(layer * 3 + 1, left.x, left.y));
            nodes.add(new Node(layer * 3 + 2, right.x, right.y));
        }

        return nodes;
    }

    private List<Edge> connectTriangles(List<Node> nodes, int layers) {
        List<Edge> edges = new ArrayList<>();

        // Connect each triangle's edges
        for (int layer = 0; layer < layers; layer++) {
            int baseIdx = layer * NODES_PER_TRIANGLE;

            // Triangle edges
            edges.add(new Edge(nodes.get(baseIdx), nodes.get(baseIdx + 1))); // Top-left
            edges.add(new Edge(nodes.get(baseIdx + 1), nodes.get(baseIdx + 2))); // Left-right
            edges.add(new Edge(nodes.get(baseIdx + 2), nodes.get(baseIdx))); // Right-top

            // Connect to inner triangle (if exists)
            if (layer < layers - 1) {
                int innerBase = (layer + 1) * NODES_PER_TRIANGLE;

                // Connect corresponding vertices
                edges.add(new Edge(nodes.get(baseIdx), nodes.get(innerBase))); // Top
                edges.add(new Edge(nodes.get(baseIdx + 1), nodes.get(innerBase + 1))); // Left
                edges.add(new Edge(nodes.get(baseIdx + 2), nodes.get(innerBase + 2))); // Right

                // Add cross connections for visual interest
                edges.add(new Edge(nodes.get(baseIdx), nodes.get(innerBase + 1)));
                edges.add(new Edge(nodes.get(baseIdx + 1), nodes.get(innerBase + 2)));
                edges.add(new Edge(nodes.get(baseIdx + 2), nodes.get(innerBase)));
            }
        }

        return edges;
    }
}