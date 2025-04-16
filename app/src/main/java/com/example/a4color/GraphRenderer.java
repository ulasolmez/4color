package com.example.a4color;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GraphRenderer {
    private static final int NODE_RADIUS = 40;
    private static final int NODE_STROKE_WIDTH = 3;
    private static final int EDGE_STROKE_WIDTH = 5;

    private final Paint nodePaint;
    private final Paint edgePaint;

    public GraphRenderer() {
        nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(EDGE_STROKE_WIDTH);
    }

    public void drawGraph(Canvas canvas, Graph graph) {
        canvas.drawColor(Color.rgb(255, 255, 204)); // Light yellow background

        // Draw edges first
        for (Edge edge : graph.getEdges()) {
            canvas.drawLine(
                    edge.getStart().getPosition().x,
                    edge.getStart().getPosition().y,
                    edge.getEnd().getPosition().x,
                    edge.getEnd().getPosition().y,
                    edgePaint
            );
        }

        // Draw nodes
        for (Node node : graph.getNodes()) {
            // Fill
            nodePaint.setColor(node.getColor());
            nodePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                    node.getPosition().x,
                    node.getPosition().y,
                    NODE_RADIUS,
                    nodePaint
            );

            // Border
            nodePaint.setStyle(Paint.Style.STROKE);
            nodePaint.setColor(Color.BLACK);
            nodePaint.setStrokeWidth(NODE_STROKE_WIDTH);
            canvas.drawCircle(
                    node.getPosition().x,
                    node.getPosition().y,
                    NODE_RADIUS,
                    nodePaint
            );
        }
    }
}

