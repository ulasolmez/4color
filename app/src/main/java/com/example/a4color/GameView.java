package com.example.a4color;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.Objects;

public class GameView extends View {
    private GraphRenderer renderer;
    private GraphTouchHandler touchHandler;
    private GameState gameState;
    private ColorPicker colorPicker;
    private GameEventListener gameEventListener;


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

    public GameView(Context context, GameState gameState, ColorPicker colorPicker) {
        super(context);
        this.gameState = gameState;
        this.colorPicker = colorPicker;
        this.renderer = new GraphRenderer();
        this.touchHandler = new GraphTouchHandler();
        setupTouchHandling();
    }

    private void init(@Nullable AttributeSet attrs) {
        renderer = new GraphRenderer();
        touchHandler = new GraphTouchHandler();
        colorPicker = new MaterialColorPicker();
        setupTouchHandling();
    }

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

    private void checkLevelCompletion() {
        if (gameState != null && gameState.getCurrentLevel() != null) {
            boolean isComplete = gameState.getCurrentLevel().isColoredCorrectly();
            Log.d("GameView", "Level completion check: " + isComplete);

            if (isComplete && gameEventListener != null) {
                gameEventListener.onLevelCompleted();
            }
        }
    }

    private void setupTouchHandling() {
        touchHandler.setOnNodeSelectedListener(node -> {
            try {
                if (colorPicker != null && getContext() != null) {
                    colorPicker.showColorPicker(getContext(), color -> {
                        if (node != null && node.canAcceptColor(color)) {
                            // Animate color change
                            ValueAnimator anim = ValueAnimator.ofArgb(node.getColor(), color);
                            anim.setDuration(300);
                            anim.addUpdateListener(animation -> {
                                node.setColor((int) animation.getAnimatedValue());
                                invalidate();
                            });
                            anim.start();

                            // Check level completion after animation
                            anim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    checkLevelCompletion();
                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("GameView", "Color picker error", e);
            }
        });
    }

    private void animateColorChange(Node node, int newColor, Runnable onComplete) {
        ValueAnimator anim = ValueAnimator.ofArgb(node.getColor(), newColor);
        anim.setDuration(300);
        anim.addUpdateListener(animation -> {
            node.setColor((int) animation.getAnimatedValue());
            invalidate();
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onComplete.run();
            }
        });
        anim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderer.drawGraph(canvas, gameState.getCurrentLevel());
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // You can use these dimensions in your color picker positioning
    }

    private void showColorPicker() {
        // Ensure we have valid dimensions
        post(() -> {
            if (colorPicker != null) {
                colorPicker.showColorPicker(getContext(), color -> {
                    // Handle color selection
                });
            }
        });
    }

}