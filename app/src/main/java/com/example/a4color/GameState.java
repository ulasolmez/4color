package com.example.a4color;

import android.graphics.Color;

public class GameState {
    private Level currentLevel;
    private boolean isCompleted;

    public GameState(Level initialLevel) {
        this.currentLevel = initialLevel;
    }

    public Graph getCurrentLevel() {
        return (Graph) currentLevel;
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
        this.isCompleted = false;
    }

    public void applyColor(Node node, int color) {
        if (node == null) return;
        node.setColor(color);
    }

    public boolean checkWinCondition() {
        for (Node node : currentLevel.getNodes()) {
            if (node.getColor() == Color.WHITE) {
                return false;
            }
        }
        for (Edge edge : currentLevel.getEdges()) {
            if (edge.getStart().getColor() == edge.getEnd().getColor()) {
                return false;
            }
        }
        isCompleted = true;
        return true;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    private static class ColorAction {
        private final Node node;
        private final int previousColor;

        public ColorAction(Node node, int previousColor) {
            this.node = node;
            this.previousColor = previousColor;
        }

        public Node getNode() {
            return node;
        }

        public int getPreviousColor() {
            return previousColor;
        }
    }
}
