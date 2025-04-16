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
        int nodeCount = BASE_SIZE + (levelNumber);
        nodeCount = Math.min(nodeCount, 28); // Cap at 15 nodes

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

        // Calculate row information
        int[] rowInfo = calculateRowInfo(nodes.size());
        int totalRows = rowInfo.length;

        for (int row = 0; row < totalRows; row++) {
            int startIndex = rowInfo[row];
            int nodesInRow = row + 1;
            int nextRowStart = (row < totalRows - 1) ? rowInfo[row + 1] : -1;

            for (int posInRow = 0; posInRow < nodesInRow; posInRow++) {
                int currentIndex = startIndex + posInRow;
                if (currentIndex >= nodes.size()) continue;

                // Connect to right neighbor in same row
                if (posInRow < nodesInRow - 1) {
                    int rightIndex = currentIndex + 1;
                    if (rightIndex < nodes.size() && rightIndex < startIndex + nodesInRow) {
                        addEdgeIfNotExists(edges, nodes.get(currentIndex), nodes.get(rightIndex));
                    }
                }

                // Connect to nodes below (left and right children)
                if (nextRowStart != -1) {
                    // Left child below
                    int leftChildIndex = nextRowStart + posInRow;
                    if (leftChildIndex < nodes.size()) {
                        addEdgeIfNotExists(edges, nodes.get(currentIndex), nodes.get(leftChildIndex));
                    }

                    // Right child below
                    int rightChildIndex = leftChildIndex + 1;
                    if (rightChildIndex < nodes.size() &&
                            rightChildIndex < nextRowStart + (row + 2)) { // Stay within next row
                        addEdgeIfNotExists(edges, nodes.get(currentIndex), nodes.get(rightChildIndex));
                    }
                }
            }
        }

        // Ensure all nodes are connected (for cases where we have partial rows)
        ensureAllNodesConnected(nodes, edges);

        return edges;
    }
    private int[] calculateRowInfo(int totalNodes) {
        List<Integer> rowStarts = new ArrayList<>();
        int accumulated = 0;
        int row = 0;

        while (accumulated < totalNodes) {
            rowStarts.add(accumulated);
            row++;
            accumulated += row;
        }

        // Convert to array
        int[] result = new int[rowStarts.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = rowStarts.get(i);
        }

        return result;
    }

    private void addEdgeIfNotExists(List<Edge> edges, Node a, Node b) {
        if (!hasEdge(edges, a, b)) {
            edges.add((Edge) new BasicEdge(a, b));
        }
    }

    private boolean hasEdge(List<Edge> edges, Node a, Node b) {
        for (Edge edge : edges) {
            if ((edge.getStart().equals(a) && edge.getEnd().equals(b)) ||
                    (edge.getStart().equals(b) && edge.getEnd().equals(a))) {
                return true;
            }
        }
        return false;
    }

    private void ensureAllNodesConnected(List<Node> nodes, List<Edge> edges) {
        // Simple solution - connect orphan nodes to first node
        boolean[] connected = new boolean[nodes.size()];

        // Mark all nodes connected by edges
        for (Edge edge : edges) {
            connected[nodes.indexOf(edge.getStart())] = true;
            connected[nodes.indexOf(edge.getEnd())] = true;
        }

        // Connect any unconnected nodes to the first node
        Node firstNode = nodes.get(0);
        for (int i = 1; i < nodes.size(); i++) {
            if (!connected[i]) {
                edges.add((Edge) new BasicEdge(firstNode, nodes.get(i)));
            }
        }
    }

}