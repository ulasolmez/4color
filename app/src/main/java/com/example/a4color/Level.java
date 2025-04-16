package com.example.a4color;

import android.graphics.Color;
import android.graphics.PointF;

import java.util.List;

/**
 * Represents a graph level in the Four Color Theorem game.
 * Contains nodes and edges that define the graph structure.
 */
public interface Level {
    /**
     * @return List of all nodes in this level
     */
    List<Node> getNodes();

    /**
     * @return List of all edges in this level
     */
    List<Edge> getEdges();

    /**
     * Checks if the level is colored according to Four Color Theorem rules:
     * 1. All nodes must be colored (no white nodes)
     * 2. No adjacent nodes share the same color
     *
     * @return true if coloring is valid, false otherwise
     */
    boolean isColoredCorrectly();

    /**
     * Optional: Resets all nodes to uncolored state (white)
     */
    default void resetColors() {
        for (Node node : getNodes()) {
            node.setColor(Color.WHITE);
        }
    }

    /**
     * Optional: Calculates the center point of all nodes
     * Useful for camera positioning/zoom
     */
    default PointF calculateCenter() {
        float sumX = 0, sumY = 0;
        List<Node> nodes = getNodes();
        for (Node node : nodes) {
            sumX += node.getPosition().x;
            sumY += node.getPosition().y;
        }
        return new PointF(sumX / nodes.size(), sumY / nodes.size());
    }
}