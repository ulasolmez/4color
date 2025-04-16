package com.example.a4color;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MaterialColorPicker implements ColorPicker {
    private static final int[] COLORS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW
    };
    private static final String[] COLOR_NAMES = {"Red", "Green", "Blue", "Yellow"};

    @Override
    public void showColorPicker(Context context, final OnColorSelectedListener listener) {
        if (!(context instanceof Activity)) return;

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Choose a Color")
                .setAdapter(new ColorArrayAdapter(context, COLOR_NAMES), (dialog, which) -> {
                    if (listener != null) {
                        listener.onColorSelected(COLORS[which]);
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.show();
    }

    private static class ColorArrayAdapter extends ArrayAdapter<String> {
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
}
