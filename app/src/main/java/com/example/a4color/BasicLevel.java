package com.example.a4color;

import android.graphics.Color;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

public class BasicLevel implements Level {
    private final List<Node> nodes;
    private final List<Edge> edges;

    public BasicLevel(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes != null ? new ArrayList<>(nodes) : new ArrayList<>();
        this.edges = edges != null ? new ArrayList<>(edges) : new ArrayList<>();
        setupNeighbors();
    }

    private void setupNeighbors() {
        for (Edge edge : edges) {
            edge.getStart().getNeighbors().add(edge.getEnd());
            edge.getEnd().getNeighbors().add(edge.getStart());
        }
    }

    @Override
    public List<Node> getNodes() {
        return new ArrayList<>(nodes); // Return defensive copy
    }

    @Override
    public List<Edge> getEdges() {
        return new ArrayList<>(edges); // Return defensive copy
    }

    @Override
    public boolean isColoredCorrectly() {
        // Check all nodes are colored (none are white)
        for (Node node : nodes) {
            if (node.getColor() == Color.WHITE) {
                return false;
            }
        }

        // Check no adjacent nodes have same color
        for (Edge edge : edges) {
            if (edge.getStart().getColor() == edge.getEnd().getColor()) {
                return false;
            }
        }

        return true;
    }

    // Optional helper methods
    public void resetColors() {
        for (Node node : nodes) {
            node.setColor(Color.WHITE);
        }
    }

    public PointF calculateCenter() {
        float sumX = 0, sumY = 0;
        for (Node node : nodes) {
            sumX += node.getPosition().x;
            sumY += node.getPosition().y;
        }
        return new PointF(sumX / nodes.size(), sumY / nodes.size());
    }
}