 package com.example.a4color;

import java.util.ArrayList;
import java.util.List;

public class WheelGraphGenerator implements LevelGenerator {
     @Override
     public Level generateLevel(int levelNumber, int width, int height) {
         // Calculate size based on level
         int rimSize = 3 + levelNumber; // Minimum 4 nodes for a wheel
         if (rimSize > 12) rimSize = 12; // Cap at 12 nodes

         List<Node> nodes = new ArrayList<>();
         List<Edge> edges = new ArrayList<>();

         float centerX = width / 2f;
         float centerY = height / 2f;
         float radius = Math.min(width, height) * 0.4f;

         // 1. Create center node
         Node center = new BasicNode(0, centerX, centerY);
         nodes.add(center);

         // 2. Create rim nodes in a circle
         for (int i = 1; i <= rimSize; i++) {
             double angle = 2 * Math.PI * (i-1) / rimSize;
             float x = centerX + (float)(radius * Math.cos(angle));
             float y = centerY + (float)(radius * Math.sin(angle));
             Node rimNode = new BasicNode(i, x, y);
             nodes.add(rimNode);

             // Connect to center (spokes)
             edges.add(new BasicEdge(center, rimNode));
         }

         // 3. Connect rim nodes in a cycle
         for (int i = 1; i <= rimSize; i++) {
             Node current = nodes.get(i);
             Node next = nodes.get((i % rimSize) + 1); // Wraps around
             edges.add(new BasicEdge(current, next));
         }

         return new BasicLevel(nodes, edges);
     }
 }
