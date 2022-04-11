package com.example.timeflies.utils;

import android.widget.TextView;

public class ColorUtils {



    /**
     * 合成指定颜色、指定不透明度的颜色，
     * 0:完全透明，1：不透明
     * @param color
     * @param alpha 0:完全透明，1：不透明
     * @return
     */
    public static int alphaColor(int color,float alpha){
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & color;
        return a + rgb;
    }



}
