package com.example.a4color;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
        if (!(context instanceof Activity)) {
            Log.e("ColorPicker", "Context is not an Activity");
            return;
        }

        // Create a GridView for horizontal color selection
        GridView gridView = new GridView(context);
        gridView.setNumColumns(COLORS.length); // Horizontal layout
        gridView.setHorizontalSpacing(16); // 16dp between colors
        gridView.setVerticalSpacing(16);
        gridView.setAdapter(new ColorAdapter(context));

        // Create dialog with proper theme
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.ColorPickerDialog)
                .setView(gridView)
                .setNegativeButton("Cancel", null)
                .create();

        // Set transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            listener.onColorSelected(COLORS[position]);
            dialog.dismiss();
        });

        dialog.show();
    }

    private class ColorAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private final int itemSize;

        ColorAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.itemSize = (int) (64 * context.getResources().getDisplayMetrics().density);
        }

        @Override public int getCount() { return COLORS.length; }
        @Override public Object getItem(int pos) { return COLORS[pos]; }
        @Override public long getItemId(int pos) { return pos; }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.color_circle_item, parent, false);
            }

            ImageView colorView = convertView.findViewById(R.id.colorCircle);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(COLORS[pos]);
            drawable.setStroke(4, Color.WHITE); // White border
            colorView.setBackground(drawable);

            return convertView;
        }
    }
}
