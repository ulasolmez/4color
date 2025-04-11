package com.example.a4color;

import android.graphics.Color;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public static final int DEFAULT_COLOR = Color.WHITE;
    public static final int[] POSSIBLE_COLORS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW
    };
    private int id;
    private PointF position;
    private int color;
    private List<Node> neighbors;

    public Node(int id, float x, float y) {
        this.id = id;
        this.position = new PointF(x, y);
        this.color = DEFAULT_COLOR;
        this.neighbors = new ArrayList<>();
    }
    public boolean canAcceptColor(int newColor) {
        for (Node neighbor : neighbors) {
            if (neighbor.getColor() == newColor) {
                return false;
            }
        }
        return true;
    }

    // Get list of adjacent nodes
    public List<Node> getNeighbors() {
        return neighbors;
    }

    // Get position for drawing
    public PointF getPosition() {
        return position;
    }

    // Get current color
    public int getColor() {
        return color;
    }

    // Set new color
    public void setColor(int color) {
        this.color = color;
    }

    // Add neighbor (used when building edges)
    public void addNeighbor(Node node) {
        if (!neighbors.contains(node)) {
            neighbors.add(node);
        }
    }
    public int getId() {
        return id;
    }

}