package com.example.a4color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProgressiveGraphGenerator implements LevelGenerator{
    private static final int BASE_NODES = 4;
    private static final float COMPLEXITY_FACTOR = 1.3f;
    @Override
    public Level generateLevel(int levelNumber, int width, int height) {
        int targetNodes = BASE_NODES + (int)(levelNumber * COMPLEXITY_FACTOR);
        int targetEdges = calculateTargetEdges(levelNumber, targetNodes);

        List<Node> nodes = generateNodeLayout(levelNumber, targetNodes, width, height);
        List<Edge> edges = buildEdgesWithProgression(nodes, levelNumber, targetEdges, width, height);

        return new Level(nodes, edges);
    }

    private static int calculateTargetEdges(int level, int nodeCount) {
        // Gradually increase edge density
        float baseDensity = 1.2f + (level * 0.05f);
        return (int)(nodeCount * Math.min(baseDensity, 2.5f)); // Cap at 2.5x
    }

    private static List<Node> generateNodeLayout(int level, int count, int width, int height) {
        List<Node> nodes = new ArrayList<>();
        float centerX = width / 2f;
        float centerY = height / 2f;
        float radius = Math.min(width, height) * 0.4f;

        // Different layouts based on level progression

            // Circular layout for early levels
            for (int i = 0; i < count; i++) {
                double angle = 2 * Math.PI * i / count;
                float x = centerX + (float)(radius * Math.cos(angle));
                float y = centerY + (float)(radius * Math.sin(angle));
                nodes.add(new Node(i, x, y));
            }

        return nodes;
    }

    private static List<Edge> buildEdgesWithProgression(List<Node> nodes,
                                                        int level,
                                                        int targetEdges,
                                                        int width,
                                                        int height ) {
        List<Edge> edges = new ArrayList<>();
        Random random = new Random(level * 1000L); // Seed for consistency

        // 1. Create base connections (ensures graph is planar and connected)
        for (int i = 1; i < nodes.size(); i++) {
            int connectTo = random.nextInt(i);
            edges.add(new Edge(nodes.get(i), nodes.get(connectTo)));
        }

        // 2. Add complexity based on level
        int extraEdges = targetEdges - edges.size();
        int attempts = 0;

        while (edges.size() < targetEdges && attempts < 1000) {
            attempts++;
            Node a = nodes.get(random.nextInt(nodes.size()));
            Node b = nodes.get(random.nextInt(nodes.size()));

            if (a == b || containsEdge(edges, a, b)) continue;

            // Level-based edge validation
            if (level < 5) {
                // Early levels - only allow edges between nearby nodes
                if (distance(a, b) > Math.min(width, height) * 0.4f) continue;
            }

            edges.add(new Edge(a, b));
        }

        return edges;
    }

    private static boolean containsEdge(List<Edge> edges, Node a, Node b) {
        for (Edge e : edges) {
            if ((e.getStart() == a && e.getEnd() == b) ||
                    (e.getStart() == b && e.getEnd() == a)) {
                return true;
            }
        }
        return false;
    }

    private static float distance(Node a, Node b) {
        return (float)Math.hypot(
                a.getPosition().x - b.getPosition().x,
                a.getPosition().y - b.getPosition().y
        );
    }
}