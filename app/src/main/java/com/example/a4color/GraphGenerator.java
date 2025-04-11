package com.example.a4color;

import android.graphics.PointF;
import java.util.*;
/*
public class GraphGenerator {
    private static final int MAX_SUBDIVISIONS = 3; // Controls complexity
    private static final float MIN_EDGE_LENGTH = 100f;

    public static Level generatePlanarGraph(int width, int height) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        // Create initial triangle that fills 80% of screen
        PointF center = new PointF(width/2f, height/2f);
        float size = Math.min(width, height) * 0.4f;

        Node n1 = new Node(0, center.x - size, center.y + size);
        Node n2 = new Node(1, center.x + size, center.y + size);
        Node n3 = new Node(2, center.x, center.y - size);

        nodes.addAll(Arrays.asList(n1, n2, n3));
        edges.add(new Edge(n1, n2));
        edges.add(new Edge(n2, n3));
        edges.add(new Edge(n3, n1));

        // List of triangular faces to subdivide
        Queue<TriangleFace> faces = new LinkedList<>();
        faces.add(new TriangleFace(n1, n2, n3));

        // Recursive subdivision
        Random rand = new Random();
        int nodeId = 3;

        for (int i = 0; i < MAX_SUBDIVISIONS; i++) {
            int facesToProcess = faces.size();
            for (int j = 0; j < facesToProcess; j++) {
                TriangleFace face = faces.poll();

                // 70% chance to subdivide a face
                if (rand.nextFloat() < 0.7 && validFaceForSubdivision(face)) {
                    // Create new node at centroid
                    PointF centroid = calculateCentroid(face);
                    Node newNode = new Node(nodeId++, centroid.x, centroid.y);
                    nodes.add(newNode);

                    // Create new edges and faces
                    edges.add(new Edge(newNode, face.a));
                    edges.add(new Edge(newNode, face.b));
                    edges.add(new Edge(newNode, face.c));

                    faces.add(new TriangleFace(face.a, face.b, newNode));
                    faces.add(new TriangleFace(face.b, face.c, newNode));
                    faces.add(new TriangleFace(face.c, face.a, newNode));
                } else {
                    faces.add(face);
                }
            }
        }

        return new Level(nodes, edges);
    }

    private static boolean validFaceForSubdivision(TriangleFace face) {
        // Check if face is large enough to subdivide
        return distance(face.a, face.b) > MIN_EDGE_LENGTH &&
                distance(face.b, face.c) > MIN_EDGE_LENGTH &&
                distance(face.c, face.a) > MIN_EDGE_LENGTH;
    }

    private static PointF calculateCentroid(TriangleFace face) {
        return new PointF(
                (face.a.getPosition().x + face.b.getPosition().x + face.c.getPosition().x) / 3,
                (face.a.getPosition().y + face.b.getPosition().y + face.c.getPosition().y) / 3
        );
    }

    private static float distance(Node a, Node b) {
        return (float) Math.hypot(
                a.getPosition().x - b.getPosition().x,
                a.getPosition().y - b.getPosition().y
        );
    }

    private static class TriangleFace {
        final Node a, b, c;

        TriangleFace(Node a, Node b, Node c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}

 */