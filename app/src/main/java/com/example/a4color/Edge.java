package com.example.a4color;

public class Edge {
    private Node start;
    private Node end;

    public Edge(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    // Get starting node of the edge
    public Node getStart() {
        return start;
    }

    // Get ending node of the edge
    public Node getEnd() {
        return end;
    }
}