package com.example.a4color;

import android.graphics.Color;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class BasicNode implements Node{
    private final int id;
    private final PointF position;
    private int color;
    private final List<Node> neighbors;

    public BasicNode(int id, float x, float y) {
        this.id = id;
        this.position = new PointF(x, y);
        this.color = Color.WHITE;
        this.neighbors = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public PointF getPosition() {
        return position;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public List<Node> getNeighbors() {
        return neighbors;
    }

    @Override
    public boolean canAcceptColor(int newColor) {
        for (Node neighbor : neighbors) {
            if (neighbor.getColor() == newColor) {
                return false;
            }
        }
        return true;
    }
}
