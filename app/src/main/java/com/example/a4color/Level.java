package com.example.a4color;

import java.util.List;

public class Level {
    private List<Node> nodes;
    private List<Edge> edges;

    public Level(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;

        // Set up neighbor relationships
        for (Edge edge : edges) {
            Node start = edge.getStart();
            Node end = edge.getEnd();
            start.getNeighbors().add(end);
            end.getNeighbors().add(start);
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}