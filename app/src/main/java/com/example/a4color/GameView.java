package com.example.a4color;
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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
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

        // Debug: Print node positions
        for (Node node : nodes) {
            Log.d("GAME_VIEW", "Node position: " + node.getPosition());
        }
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

        /* Activity activity = (Activity) getContext();
        if (activity == null || activity.isFinishing()) return;

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Congratulations!")
                .setMessage("You've successfully colored the map!")
                .setPositiveButton("Next Level", (dialog, which) -> {
                    LevelManager.nextLevel();
                    nodes = LevelManager.getCurrentLevel().getNodes();
                    edges = LevelManager.getCurrentLevel().getEdges();
                    invalidate();
                })
                .setNegativeButton("Replay", (dialog, which) -> {
                    resetLevel();
                    invalidate();
                })
                .setCancelable(false)
                .show();

        */
    }



    private void loadLevel(Level level) {
        this.nodes = level.getNodes();
        this.edges = level.getEdges();
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

    private boolean isValidColor(Node node, int newColor) {
        // Check if color is one of the allowed colors
        if (newColor != Color.RED &&
                newColor != Color.GREEN &&
                newColor != Color.BLUE &&
                newColor != Color.YELLOW &&
                newColor != Color.TRANSPARENT) {
            return false;
        }

        // Check adjacent nodes
        for (Node neighbor : node.getNeighbors()) {
            if (neighbor.getColor() == newColor) {
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
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(255, 255, 204)); // Light yellow background

        // Draw edges first
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(EDGE_STROKE_WIDTH);
        for (Edge edge : edges) {
            canvas.drawLine(
                    edge.getStart().getPosition().x,
                    edge.getStart().getPosition().y,
                    edge.getEnd().getPosition().x,
                    edge.getEnd().getPosition().y,
                    edgePaint
            );
        }

        // Draw nodes
        for (Node node : nodes) {
            // Fill
            nodePaint.setColor(node.getColor());
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
            nodePaint.setStyle(Paint.Style.FILL);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Node node : nodes) {
                if (isTouchOnNode(event.getX(), event.getY(), node)) {
                    selectedNode = node;
                    showColorPicker();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean isTouchOnNode(float x, float y, Node node) {
        return Math.hypot(x - node.getPosition().x,
                y - node.getPosition().y) < 30;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }
}