package com.example.a4color;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Stack;

@SuppressLint("ViewConstructor")
public class GameView extends View {
    private static final int BASE_NODE_RADIUS = 40;
    private static final float NODE_SCALE_FACTOR = 0.03f;
    private static final float MIN_NODE_SCALE = 0.7f;
    private static final float MAX_NODE_SCALE = 1.3f;
    private ValueAnimator zoomAnimator;
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    private boolean isZooming = false;
    private float zoomFactor = 1f;
    private PointF viewOffset = new PointF(0, 0);
    private PointF lastTouch = new PointF();
    private Level currentLevel;
    private static final int NODE_RADIUS = 40;
    private static final int NODE_STROKE_WIDTH = 3;
    private static final int EDGE_STROKE_WIDTH = 5;
    private static final int[] COLORS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW
    };
    private static final String[] COLOR_NAMES = {"Red", "Green", "Blue", "Yellow"};

    private List<Node> nodes;
    private List<Edge> edges;
    private Paint nodePaint, edgePaint;
    private int selectedColor = Color.TRANSPARENT;
    private Node selectedNode;

    private Stack<ColorAction> undoStack = new Stack<>();

    public GameView(Context context, Level level) {
        super(context);
        this.currentLevel = level;
        setFocusable(true);
        setFocusableInTouchMode(true);
        // In GameView initialization

        // Initialize paints
        nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
       // nodePaint.setStyle(Paint.Style.FILL);
        edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Initialize nodes/edges
        this.nodes = level.getNodes();
        this.edges = level.getEdges();

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());

        adjustZoomForLevel();

    }

    public void setLevel(Level newLevel) {
        this.currentLevel = newLevel;
        this.nodes = newLevel.getNodes();
        this.edges = newLevel.getEdges();

        // Auto-adjust view
        adjustZoomForLevel();
        initializeNeighbors();
        invalidate();
    }



    public void animateZoom(float targetZoom) {
        if (zoomAnimator != null) {
            zoomAnimator.cancel();
        }

        zoomAnimator = ValueAnimator.ofFloat(zoomFactor, targetZoom);
        zoomAnimator.addUpdateListener(animation -> {
            zoomFactor = (float) animation.getAnimatedValue();
            invalidate();
        });
        zoomAnimator.setDuration(300);
        zoomAnimator.start();
    }

    private class ColorAction {
        Node node;
        int previousColor;

        ColorAction(Node node, int previousColor) {
            this.node = node;
            this.previousColor = previousColor;
        }
    }

    public void undoLastColor() {
        if (!undoStack.isEmpty()) {
            ColorAction action = undoStack.pop();
            action.node.setColor(action.previousColor);
            invalidate();
        }
    }
    private void init(Level level) {
        nodes = level.getNodes();
        edges = level.getEdges();
        // In GameView initialization
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(3);
    }

    private void drawBackground(Canvas canvas) {
        // Draw non-scaled background
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE); // Or your background color/image
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate positions based on new size
    }

    private void resetLevel() {
        for (Node node : nodes) {
            node.setColor(Color.WHITE);
        }
    }
    private void showWinDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Level Complete!")
                .setMessage("Great job coloring the graph!")
                .setPositiveButton("Next Level", (dialog, which) -> {
                    LevelManager.nextLevel();
                    loadLevel(LevelManager.getCurrentLevel());
                    invalidate();
                })
                .setNegativeButton("Retry", (dialog, which) -> {
                    resetLevel();
                    invalidate();
                })
                .show();
    }



    private void loadLevel(Level level) {
        this.nodes = level.getNodes();
        this.edges = level.getEdges();
        initializeNeighbors(); // Add this line
        resetLevel(); // Clear all colors
    }

    private void checkWinCondition() {
        // Check if all nodes are colored
        for (Node node : nodes) {
            if (node.getColor() == Color.WHITE) {
                return; // Found uncolored node
            }
        }

        // Check all adjacent pairs
        for (Edge edge : edges) {
            Node start = edge.getStart();
            Node end = edge.getEnd();
            if (start.getColor() == end.getColor()) {
                return; // Found conflict
            }
        }

        // If all checks passed
        showWinDialog();
    }

    private void initializeNeighbors() {
        // Clear existing neighbors
        for (Node node : nodes) {
            node.getNeighbors().clear();
        }

        // Establish neighbor relationships from edges
        for (Edge edge : edges) {
            Node start = edge.getStart();
            Node end = edge.getEnd();

            if (start != end) {  // Prevent self-neighbors
                if (!start.getNeighbors().contains(end)) {
                    start.getNeighbors().add(end);
                }
                if (!end.getNeighbors().contains(start)) {
                    end.getNeighbors().add(start);
                }
            }
        }

    }

    private boolean isValidColor(Node node, int newColor) {
        // Check if color is one of the allowed colors
        boolean validColor = false;
        for (int allowedColor : COLORS) {
            if (newColor == allowedColor) {
                validColor = true;
                break;
            }
        }
        if (!validColor) {
            return false;
        }

        // Check adjacent nodes (skip self)
        for (Node neighbor : node.getNeighbors()) {
            if (neighbor != node && neighbor.getColor() == newColor) {
                return false;
            }
        }
        return true;
    }
    private void showColorPicker() {
        Context context = getContext();
        if (!(context instanceof Activity)) return;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Choose a Color")
                .setAdapter(new ColorArrayAdapter(context, COLOR_NAMES), (dialog, which) -> {
                    if (selectedNode != null) {
                        int chosenColor = COLORS[which];
                        if (isValidColor(selectedNode, chosenColor)) {
                            undoStack.push(new ColorAction(selectedNode, selectedNode.getColor()));
                            selectedNode.setColor(chosenColor);
                            invalidate();
                            checkWinCondition();
                        } else {
                            Toast.makeText(context,
                                    "Adjacent nodes can't have the same color!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton("Undo", (dialog, which) -> undoLastColor())
                .setNegativeButton("Cancel", null);

        builder.show();
    }
    private class ColorArrayAdapter extends ArrayAdapter<String> {
        ColorArrayAdapter(Context context, String[] colors) {
            super(context, android.R.layout.simple_list_item_1, colors);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = (TextView) view;
            textView.setCompoundDrawablesWithIntrinsicBounds(
                    new ColorDrawable(COLORS[position]), null, null, null);
            textView.setCompoundDrawablePadding(30);
            return view;
        }
    }




    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background FIRST without any transformations
        drawBackground(canvas);

        // Apply zoom/pan transformations only to game elements
        canvas.save();
        canvas.translate(viewOffset.x, viewOffset.y);
        canvas.scale(zoomFactor, zoomFactor);

        // Draw edges with transformed coordinates
        for (Edge edge : edges) {
            PointF start = edge.getStart().getPosition();
            PointF end = edge.getEnd().getPosition();
            canvas.drawLine(start.x, start.y, end.x, end.y, edgePaint);
        }

        // Draw nodes with level-appropriate sizing
        float nodeScale = calculateNodeScale();
        for (Node node : nodes) {
            PointF pos = node.getPosition();
            float radius = BASE_NODE_RADIUS * nodeScale;

            // Node fill
            nodePaint.setColor(node.getColor());
            canvas.drawCircle(pos.x, pos.y, radius, nodePaint);

            // Node border
            nodePaint.setStyle(Paint.Style.STROKE);
            nodePaint.setColor(Color.BLACK);
            canvas.drawCircle(pos.x, pos.y, radius, nodePaint);
            nodePaint.setStyle(Paint.Style.FILL);
        }

        canvas.restore(); // Restore canvas state

        // Draw UI elements (like zoom percentage)
        drawUIOverlay(canvas);
    }

    private void drawUIOverlay(Canvas canvas) {
        // Draw zoom percentage
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        canvas.drawText(String.format("%d%%", (int)(zoomFactor*100)), 20, 50, textPaint);
    }

    private float calculateNodeScale() {
        int nodeCount = currentLevel.getNodes().size();
        // Scale nodes smaller as level complexity increases
        float scale = 1.0f - (nodeCount - 4) * NODE_SCALE_FACTOR;
        return Math.max(MIN_NODE_SCALE, Math.min(scale, MAX_NODE_SCALE));
    }

    private float getDynamicNodeRadius() {
        // Combine level-based scaling with zoom scaling
        return BASE_NODE_RADIUS * calculateNodeScale() * (1.0f / zoomFactor);
    }

    private void adjustZoomForLevel() {
        int nodeCount = currentLevel.getNodes().size();

        // More nodes = zoom out more
        zoomFactor = 1.0f - (nodeCount - 4) * 0.03f;
        zoomFactor = Math.max(0.6f, Math.min(zoomFactor, 1.2f));

        // Center the view
        if (getWidth() > 0 && getHeight() > 0) {
            viewOffset.set(
                    (getWidth() - getWidth() * zoomFactor) / 2,
                    (getHeight() - getHeight() * zoomFactor) / 2
            );
        }

        invalidate();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        // Only handle node selection if not zooming/panning
        if (!isZooming && event.getAction() == MotionEvent.ACTION_DOWN) {
            // Convert screen coordinates to game coordinates
            float gameX = (event.getX() - viewOffset.x) / zoomFactor;
            float gameY = (event.getY() - viewOffset.y) / zoomFactor;

            for (Node node : nodes) {
                if (isTouchOnNode(gameX, gameY, node)) {
                    selectedNode = node;
                    showColorPicker();
                    return true;
                }
            }
        }
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isZooming = true;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            zoomFactor *= scaleFactor;
            zoomFactor = Math.max(0.5f, Math.min(zoomFactor, 3.0f)); // Limit zoom range

            // Adjust view offset to zoom toward pinch center
            viewOffset.x = detector.getFocusX() - (detector.getFocusX() - viewOffset.x) * scaleFactor;
            viewOffset.y = detector.getFocusY() - (detector.getFocusY() - viewOffset.y) * scaleFactor;

            invalidate();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isZooming = false;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isZooming) {
                viewOffset.x -= distanceX;
                viewOffset.y -= distanceY;
                invalidate();
                return true;
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Reset zoom on double tap
            zoomFactor = 1f;
            viewOffset.set(0, 0);
            invalidate();
            return true;
        }
    }

    private boolean isTouchOnNode(float touchX, float touchY, Node node) {
        PointF pos = node.getPosition();
        float radius = getDynamicNodeRadius();
        return Math.hypot(touchX - pos.x, touchY - pos.y) < radius;
    }

    private void validateViewOffset() {
        float scaledWidth = getWidth() * zoomFactor;
        float scaledHeight = getHeight() * zoomFactor;

        viewOffset.x = Math.min(0, Math.max(viewOffset.x, getWidth() - scaledWidth));
        viewOffset.y = Math.min(0, Math.max(viewOffset.y, getHeight() - scaledHeight));
    }



    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }
}