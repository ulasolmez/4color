package com.example.a4color;


import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

public class TriangularGraphGenerator implements LevelGenerator {
    private static final float NODE_SPACING = 0.15f; // Relative to screen size
    private static final int BASE_SIZE = 3; // Smallest triangle

    @Override
    public Level generateLevel(int levelNumber, int width, int height) {
        // Calculate graph size based on level
        int nodeCount = BASE_SIZE + (int)(levelNumber * 0.7);
        nodeCount = Math.min(nodeCount, 15); // Cap at 15 nodes

        List<Node> nodes = createTriangularNodes(nodeCount, width, height);
        List<Edge> edges = createTriangularEdges(nodes);

        return new Level(nodes, edges);
    }

    private List<Node> createTriangularNodes(int count, int width, int height) {
        List<Node> nodes = new ArrayList<>();
        float spacing = Math.min(width, height) * NODE_SPACING;

        // Special case for single triangle
        if (count == 3) {
            nodes.add(new Node(0, (float) width /2, (float) height /3));
            nodes.add(new Node(1, (float) width /3, (float) (height * 2) /3));
            nodes.add(new Node(2, (float) (width * 2) /3, (float) (height * 2) /3));
            return nodes;
        }

        // Calculate rows needed
        int rows = (int) Math.ceil((Math.sqrt(8 * count + 1) - 1) / 2);

        // Create triangular grid
        int nodeId = 0;
        for (int row = 0; row < rows; row++) {
            int nodesInRow = row + 1;
            float y = height/4f + row * spacing;

            for (int col = 0; col < nodesInRow; col++) {
                if (nodeId >= count) break;

                float x = width/2f + (col - row/2f) * spacing;
                nodes.add(new Node(nodeId++, x, y));
            }
        }

        return nodes;
    }

    private List<Edge> createTriangularEdges(List<Node> nodes) {
        List<Edge> edges = new ArrayList<>();
        if (nodes.size() < 2) return edges;

        // Connect nodes in triangular pattern
        int[] rowStartIndices = calculateRowStarts(nodes.size());

        for (int row = 0; row < rowStartIndices.length - 1; row++) {
            int start = rowStartIndices[row];
            int nextRowStart = rowStartIndices[row + 1];
            int nodesInRow = row + 1;

            for (int pos = 0; pos < nodesInRow; pos++) {
                int current = start + pos;
                if (current >= nodes.size()) continue;

                // Connect to neighbor in same row
                if (pos < nodesInRow - 1 && (start + pos + 1) < nodes.size()) {
                    edges.add(new Edge(nodes.get(current), nodes.get(current + 1)));
                }

                // Connect to nodes below
                if (row < rowStartIndices.length - 2) {
                    int belowLeft = nextRowStart + pos;
                    int belowRight = belowLeft + 1;

                    if (belowLeft < nodes.size()) {
                        edges.add(new Edge(nodes.get(current), nodes.get(belowLeft)));
                    }
                    if (belowRight < nodes.size()) {
                        edges.add(new Edge(nodes.get(current), nodes.get(belowRight)));
                    }
                }
            }
        }

        return edges;
    }

    private int[] calculateRowStarts(int totalNodes) {
        List<Integer> starts = new ArrayList<>();
        int accumulated = 0;
        int row = 0;

        while (accumulated < totalNodes) {
            starts.add(accumulated);
            row++;
            accumulated += row;
        }

        // Convert to array
        int[] result = new int[starts.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = starts.get(i);
        }

        return result;
    }
}