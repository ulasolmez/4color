package com.example.a4color;

import android.view.MotionEvent;

import java.util.List;

public class GraphTouchHandler {
    public interface OnNodeSelectedListener {
        void onNodeSelected(Node node);
    }

    private static final float TOUCH_TOLERANCE = 30f;
    private OnNodeSelectedListener listener;

    public void setOnNodeSelectedListener(OnNodeSelectedListener listener) {
        this.listener = listener;
    }

    public boolean handleTouch(MotionEvent event, List<Node> nodes) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && listener != null) {
            for (Node node : nodes) {
                if (isTouchOnNode(event.getX(), event.getY(), node)) {
                    listener.onNodeSelected(node);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTouchOnNode(float x, float y, Node node) {
        return Math.hypot(x - node.getPosition().x,
                y - node.getPosition().y) < TOUCH_TOLERANCE;
    }
}
