package com.example.a4color;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MaterialColorPicker implements ColorPicker {
    private static final int[] COLORS = {
            Color.parseColor("#EF5350"),  // Red 400
            Color.parseColor("#66BB6A"),  // Green 400
            Color.parseColor("#42A5F5"),  // Blue 400
            Color.parseColor("#FFEE58"),  // Yellow 400
    };

    @Override
    public void showColorPicker(Context context, OnColorSelectedListener listener) {
        if (!(context instanceof Activity)) return;

        // Create a popup window instead of dialog
        PopupWindow popup = new PopupWindow(context);
        popup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);

        // Create horizontal color grid
        GridView gridView = new GridView(context);
        gridView.setNumColumns(COLORS.length);
        gridView.setAdapter(new ColorAdapter(context));

        // Calculate proper spacing
        int spacing = calculateSpacing(context);
        gridView.setHorizontalSpacing(spacing);
        gridView.setVerticalSpacing(16);

        // Set popup content
        popup.setContentView(gridView);

        // Position above bottom (with 20% margin from bottom)
        View anchor = ((Activity)context).getWindow().getDecorView();
        popup.showAtLocation(anchor, Gravity.TOP, 0, (int)(anchor.getHeight() * 0.2));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            listener.onColorSelected(COLORS[position]);
            popup.dismiss();
        });
    }

    private int calculateSpacing(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int numColors = COLORS.length;
        int colorSize = (int)(64 * metrics.density); // 64dp per color

        // Calculate spacing to center the colors
        int totalColorWidth = numColors * colorSize;
        int remainingSpace = screenWidth - totalColorWidth;

        // Distribute remaining space equally between colors
        return Math.max(16, remainingSpace / (numColors + 1)); // Minimum 16dp
    }

    private static class ColorAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private final int colorSize;

        ColorAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.colorSize = (int) (64 * context.getResources().getDisplayMetrics().density);
        }

        @Override public int getCount() { return COLORS.length; }
        @Override public Object getItem(int pos) { return COLORS[pos]; }
        @Override public long getItemId(int pos) { return pos; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.color_circle_item, parent, false);

                // Set fixed size for each color circle
                convertView.setLayoutParams(new GridView.LayoutParams(
                        colorSize,
                        colorSize
                ));
            }

            ImageView colorView = convertView.findViewById(R.id.colorCircle);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(COLORS[position]);
            drawable.setStroke(4, Color.WHITE);
            colorView.setBackground(drawable);

            return convertView;
        }
    }
}
