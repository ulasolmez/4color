package com.example.a4color;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

public class WheelGraphGenerator implements LevelGenerator {
    private static final float WHEEL_RADIUS_RATIO = 0.4f; // 40% of screen size
    private static final int MIN_RIM_SIZE = 3;
    private static final int MAX_RIM_SIZE = 12;

    @Override
    public Level generateLevel(int levelNumber, int width, int height) {

        int rimSize = calculateRimSize(levelNumber);

        List<Node> nodes = createWheelNodes(rimSize, width, height);
        List<Edge> edges = createWheelEdges(nodes, rimSize);

        return new BasicLevel(nodes, edges);
    }

    private int calculateRimSize(int levelNumber) {
        int rimSize = MIN_RIM_SIZE + levelNumber;
        return Math.min(rimSize, MAX_RIM_SIZE);
    }

    private List<Node> createWheelNodes(int rimSize, int width, int height) {
        List<Node> nodes = new ArrayList<>();
        PointF center = new PointF(width / 2f, height / 2f);
        float radius = Math.min(width, height) * WHEEL_RADIUS_RATIO;


        nodes.add(new BasicNode(0, center.x, center.y));


        for (int i = 1; i <= rimSize; i++) {
            double angle = 2 * Math.PI * (i-1) / rimSize;
            float x = center.x + (float)(radius * Math.cos(angle));
            float y = center.y + (float)(radius * Math.sin(angle));
            nodes.add(new BasicNode(i, x, y));
        }

        return nodes;
    }

    private List<Edge> createWheelEdges(List<Node> nodes, int rimSize) {
        List<Edge> edges = new ArrayList<>();
        Node center = nodes.get(0);

        for (int i = 1; i <= rimSize; i++) {
            edges.add(new BasicEdge(center, nodes.get(i)));
        }

        for (int i = 1; i <= rimSize; i++) {
            Node current = nodes.get(i);
            Node next = nodes.get((i % rimSize) + 1); // Wraps around
            edges.add(new BasicEdge(current, next));
        }

        return edges;
    }
}