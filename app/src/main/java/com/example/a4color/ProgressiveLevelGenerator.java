package com.example.a4color;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProgressiveLevelGenerator implements LevelGenerator {
    private static final int BASE_NODES = 4;
    private static final float COMPLEXITY_FACTOR = 1.3f;
    private final Random random = new Random();

    @Override
    public Level generateLevel(int levelNumber, int width, int height) {
        int nodeCount = BASE_NODES + (int)(levelNumber * COMPLEXITY_FACTOR);
        float edgeDensity = 1.2f + (levelNumber * 0.05f);
        int edgeCount = (int)(nodeCount * Math.min(edgeDensity, 2.5f));

        List<Node> nodes = generateNodeLayout(levelNumber, nodeCount, width, height);
        List<Edge> edges = buildEdges(nodes, edgeCount, width, height);

        return new BasicLevel(nodes, edges);
    }

    private List<Node> generateNodeLayout(int level, int count, int width, int height) {
        List<Node> nodes = new ArrayList<>();
        float centerX = width / 2f;
        float centerY = height / 2f;
        float radius = Math.min(width, height) * 0.4f;

        if (level < 5) {
            // Circular layout
            for (int i = 0; i < count; i++) {
                double angle = 2 * Math.PI * i / count;
                float x = centerX + (float)(radius * Math.cos(angle));
                float y = centerY + (float)(radius * Math.sin(angle));
                nodes.add(new BasicNode(i, x, y));
            }
        } else if (level < 10) {
            // Grid-like layout
            int cols = (int)Math.sqrt(count) + 1;
            for (int i = 0; i < count; i++) {
                float x = centerX - radius + (i % cols) * (2 * radius / cols);
                float y = centerY - radius + ((float) i / cols) * (2 * radius / cols);
                nodes.add(new BasicNode(i, x, y));
            }
        } else {
            // Hybrid layout with clusters
            int clusters = Math.max(2, level / 5);
            int perCluster = count / clusters;

            for (int c = 0; c < clusters; c++) {
                float clusterRadius = radius * 0.6f;
                float clusterX = centerX + (c % 2 == 0 ? -radius/3 : radius/3);
                float clusterY = centerY + (c < 2 ? -radius/3 : radius/3);

                for (int i = 0; i < perCluster; i++) {
                    double angle = 2 * Math.PI * i / perCluster;
                    float x = clusterX + (float)(clusterRadius * Math.cos(angle));
                    float y = clusterY + (float)(clusterRadius * Math.sin(angle));
                    nodes.add(new BasicNode(c * perCluster + i, x, y));
                }
            }
        }
        return nodes;
    }

    private List<Edge> buildEdges(List<Node> nodes, int targetEdges, int width, int height) {
        List<Edge> edges = new ArrayList<>();

        // 1. Create base connections (ensures graph is connected)
        for (int i = 1; i < nodes.size(); i++) {
            edges.add(createEdge(nodes.get(i), nodes.get(random.nextInt(i))));
        }

        // 2. Add complexity based on level
        int extraEdges = targetEdges - edges.size();
        int attempts = 0;

        while (edges.size() < targetEdges && attempts < 1000) {
            attempts++;
            Node a = nodes.get(random.nextInt(nodes.size()));
            Node b = nodes.get(random.nextInt(nodes.size()));

            if (a == b || containsEdge(edges, a, b)) continue;


            edges.add(createEdge(a, b));
        }

        return edges;
    }

    private Edge createEdge(Node start, Node end) {
        return new BasicEdge(start, end); // This is the correct way
    }

    private boolean containsEdge(List<Edge> edges, Node a, Node b) {
        for (Edge e : edges) {
            if ((e.getStart().equals(a) && e.getEnd().equals(b)) ||
                    (e.getStart().equals(b) && e.getEnd().equals(a))) {
                return true;
            }
        }
        return false;
    }

    private float distance(Node a, Node b) {
        return (float)Math.hypot(
                a.getPosition().x - b.getPosition().x,
                a.getPosition().y - b.getPosition().y
        );
    }
}