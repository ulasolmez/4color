package com.example.a4color;

import android.graphics.PointF;

import java.util.List;

public interface Node {
    int getId();
    PointF getPosition();
    int getColor();
    void setColor(int color);
    List<Node> getNeighbors();
    boolean canAcceptColor(int newColor);
}
