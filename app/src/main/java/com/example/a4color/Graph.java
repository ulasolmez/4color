package com.example.a4color;

import java.util.List;

public interface Graph {
    List<Node> getNodes();
    List<Edge> getEdges();
    boolean isColoredCorrectly();
}
