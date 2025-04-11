package com.example.a4color;

import android.graphics.Color;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private PointF position;
    private int color;
    private List<Node> neighbors;

    public Node(int id, float x, float y) {
        this.id = id;
        this.position = new PointF(x, y);
        this.color = Color.WHITE;
        this.neighbors = new ArrayList<>();
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