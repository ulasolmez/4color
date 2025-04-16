package com.example.a4color;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MaterialColorPicker implements ColorPicker {
    private static final int[] COLORS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW
    };
    private static final String[] COLOR_NAMES = {"Red", "Green", "Blue", "Yellow"};

    @Override
    public void showColorPicker(Context context, OnColorSelectedListener listener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Choose a color")
                .setAdapter(new ColorGridAdapter(context, COLORS), (dialog, which) -> {
                    if (listener != null) {
                        listener.onColorSelected(COLORS[which]);
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Optional: Customize dialog window
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.dialog_background);
        }
    }

    private static class ColorGridAdapter extends BaseAdapter {
        private final Context context;
        private final int[] colors;

        public ColorGridAdapter(Context context, int[] colors) {
            this.context = context;
            this.colors = colors;
        }

        @Override
        public int getCount() { return colors.length; }

        @Override
        public Object getItem(int position) { return colors[position]; }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView != null ? convertView :
                    LayoutInflater.from(context).inflate(R.layout.color_grid_item, parent, false);

            ImageView colorView = view.findViewById(R.id.colorView);
            colorView.setBackgroundColor(colors[position]);

            // Add circular shape
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(colors[position]);
            drawable.setStroke(4, Color.BLACK);
            colorView.setBackground(drawable);

            return view;
        }
    }
}
