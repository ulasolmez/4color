package com.example.a4color;

import android.content.Context;

public interface ColorPicker {
    void showColorPicker(Context context, OnColorSelectedListener listener);

    interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}
