package com.example.a4color;

import android.graphics.Color;
import android.graphics.PointF;

import java.util.List;

public interface Level {
    List<Node> getNodes();
    List<Edge> getEdges();
    boolean isColoredCorrectly();
}