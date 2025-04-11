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
        // Ensure we have an activity context
        Context context = getContext();
        if (!(context instanceof Activity)) {
            return;
        }

        // Create color options (using standard Material Design colors)
        final int[] colors = {
                Color.RED,    // 0
                Color.GREEN,  // 1
                Color.BLUE,   // 2
                Color.YELLOW // 3
        };
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Choose a Color")
                .setItems(new String[]{"Red", "Green", "Blue", "Yellow"}, (dialog, which) -> {
                    if (selectedNode != null) {
                        int chosenColor = colors[which];
                        if (isValidColor(selectedNode, chosenColor)) {
                            // UNDO FUNCTIONALITY ADDED HERE
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
                // ADD UNDO BUTTON HERE
                .setNeutralButton("Undo", (dialog, which) -> undoLastColor())
                .setNegativeButton("Cancel", null);


        // Create dialog using MaterialAlertDialogBuilder for better theming
   /*     MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Choose a Color")
                .setItems(new String[]{"Red", "Green", "Blue", "Yellow"},
                        (dialog, which) -> {
                            if (selectedNode != null) {
                                int chosenColor = colors[which];
                                if (isValidColor(selectedNode, chosenColor)) {
                                    selectedNode.setColor(chosenColor);
                                    invalidate(); // Redraw the view
                                    checkWinCondition();
                                } else {
                                    Toast.makeText(context,
                                            "Adjacent nodes can't have the same color!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel", null);
                */


        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Optional: Add color preview indicators
        ListView listView = dialog.getListView();
        if (listView != null) {
            listView.setAdapter(new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_list_item_1,
                    new String[]{"Red", "Green", "Blue", "Yellow"}
            ) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    // Add color indicator
                    ImageView indicator = new ImageView(context);
                    indicator.setImageDrawable(new ColorDrawable(colors[position]));
                    indicator.setPadding(0, 0, 30, 0);

                    ((TextView) view).setCompoundDrawablesRelativeWithIntrinsicBounds(
                            indicator.getDrawable(), null, null, null);
                    return view;
                }
            });
        }
    }




    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Set background
        canvas.drawColor(Color.rgb(255,255,204));

        // Draw edges first
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(5f);
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
        nodePaint.setStyle(Paint.Style.FILL);
        for (Node node : nodes) {
            nodePaint.setColor(node.getColor());
            canvas.drawCircle(
                    node.getPosition().x,
                    node.getPosition().y,
                    40,  // Radius in pixels
                    nodePaint
            );

            // Add stroke border
            nodePaint.setStyle(Paint.Style.STROKE);
            nodePaint.setColor(Color.BLACK);
            nodePaint.setStrokeWidth(3f);
            canvas.drawCircle(
                    node.getPosition().x,
                    node.getPosition().y,
                    40,
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