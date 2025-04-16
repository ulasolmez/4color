package com.example.a4color;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class GameView extends View {
    private GraphRenderer renderer;
    private GraphTouchHandler touchHandler;
    private GameState gameState;
    private ColorPicker colorPicker;
    private GameEventListener gameEventListener;

    // Constructors
    public GameView(Context context) {
        super(context);
        init(null);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public GameView(BaseGameActivity context, GameState gameState, ColorPicker colorPicker) {
        super(context);
    }

    private void init(@Nullable AttributeSet attrs) {
        renderer = new GraphRenderer();
        touchHandler = new GraphTouchHandler();
        colorPicker = new MaterialColorPicker();
        setupTouchHandling();
    }

    // Public methods to set dependencies
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        invalidate();
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
    }

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }

    private void setupTouchHandling() {
        touchHandler.setOnNodeSelectedListener(node -> {
            if (colorPicker != null) {
                colorPicker.showColorPicker(getContext(), color -> {
                    if (gameState != null && node.canAcceptColor(color)) {
                        gameState.applyColor(node, color);
                        invalidate();

                        if (gameState.checkWinCondition() && gameEventListener != null) {
                            gameEventListener.onLevelCompleted();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gameState != null && gameState.getCurrentLevel() != null) {
            renderer.drawGraph(canvas, gameState.getCurrentLevel());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return (gameState != null &&
                touchHandler.handleTouch(event, gameState.getCurrentLevel().getNodes()))
                || super.onTouchEvent(event);
    }

    public interface GameEventListener {
        void onLevelCompleted();
    }
}