package com.example.imgpro.ImageModifications.convolution;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

public class HistogramEqualization extends AbstractImageModificationAsyncTask {
    private int bitmapWidth;
    private int bitmapHeight;
    private float totalSize;
    private int valueHistogram[];
    private int cdfMin = -1;
    private float hsvPixels[][];
    private int cdf[];


    public HistogramEqualization(Bitmap src, Activity activity) {
        super(src, activity);
        bitmapWidth = src.getWidth();
        bitmapHeight = src.getHeight();
        valueHistogram = new int[256];
        totalSize = bitmapHeight * bitmapWidth;
        hsvPixels = new float[bitmapWidth * bitmapHeight][3];
    }


    @Override
    protected Bitmap doInBackground(String... params) {

        long startTime = System.currentTimeMillis();

        Bitmap result = src.copy(Bitmap.Config.ARGB_8888,true);

        int[] pixels = new int[bitmapWidth * bitmapHeight];
        result.getPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        for (int i = 0; i < bitmapWidth * bitmapHeight; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            // Store all HSV value to save on computing power
            Color.RGBToHSV(r, g, b, hsvPixels[i]);

            // Save HSV Value in a 0-255 range
            int rangedHSVvalue = hueValueToColorRange(hsvPixels[i][2]);
            valueHistogram[rangedHSVvalue]++;
        }

        generateCDF();

        for (int i = 0; i < bitmapWidth * bitmapHeight; i++) {
            float value = hsvPixels[i][2];
            int range = hueValueToColorRange(value);
            int histoEqualValue = histoEqual(range);
            float changedValue = colorRangeToHueValue(histoEqualValue);
            hsvPixels[i][2] = changedValue;

            float[] hsv = hsvPixels[i];
            pixels[i] = Color.HSVToColor(hsv);
        }

        result.setPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        Log.i("HistEqual", "HistEqual Duration: " + elapsedTime);
        return result;
    }


    private int histoEqual(int v) {
        int cdfValue = cdf[v];
        return Math.round(((cdfValue - cdfMin) / totalSize) * 255);
    }


    private void generateCDF() {
        boolean foundMin = false;
        if (cdf == null) {
            cdf = new int[256];
            int total = 0;
            for (int i = 0; i < valueHistogram.length; i++) {
                if(!foundMin && valueHistogram[i] != 0) {
                    cdfMin = valueHistogram[i];
                    foundMin = true;
                }
                total += valueHistogram[i];
                cdf[i] = total;
            }
        }

    }


    private float colorRangeToHueValue(int range) {
        return range / 255f;
    }


    private int hueValueToColorRange(float value) {
        return (int) Math.floor(value * 255);
    }
}
