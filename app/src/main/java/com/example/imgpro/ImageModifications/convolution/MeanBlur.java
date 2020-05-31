package com.example.imgpro.ImageModifications.convolution;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

public class MeanBlur extends AbstractImageModificationAsyncTask {
    private final int filterSize;

    public MeanBlur(Bitmap src, BlurValues filterSize, Activity activity) {
        super(src, activity);

        this.filterSize = (filterSize.ordinal() * 2) + 3;
    }



    private int getAverage(int[][] pixels2D) {
        int arrayHeight = pixels2D.length;
        int arrayWidth = pixels2D[0].length;
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        Double totalPixels = (double) (arrayHeight * arrayWidth);

        for (int y = 0; y < arrayHeight; y++) {
            for (int x = 0; x < arrayWidth; x++) {
                int pixel = pixels2D[y][x];
                totalRed += Color.red(pixel);
                totalGreen += Color.green(pixel);
                totalBlue += Color.blue(pixel);
            }
        }
        Double finalRed = totalRed / totalPixels;
        Double finalGreen = totalGreen / totalPixels;
        Double finalBlue = totalBlue / totalPixels;

        return Color.rgb(finalRed.intValue(), finalGreen.intValue(), finalBlue.intValue());
    }


    private int[][] get2DPixels(int pixels[], int width, int height) {
        int[][] newPixels = new int[height][width];
        for (int y = 0; y < height; y++) {
            System.arraycopy(pixels, y * width, newPixels[y], 0, width); // Copying each set of "width" as a row
        }
        return newPixels;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        long startTime = System.currentTimeMillis();

        Bitmap result = src.copy(Bitmap.Config.ARGB_8888, true);

        int imgHeight = result.getHeight();
        int imgWidth = result.getWidth();

        // Getting each pixel in the Bitmap
        int[] pixels = new int[imgWidth * imgHeight];
        result.getPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);

        // Copying the array to keep the old pixels on the borders
        int[] newPixels = new int[imgWidth * imgHeight];
        System.arraycopy(pixels, 0, newPixels, 0, pixels.length);

        // Getting a 2D array to pick a sub array more easily
        int[][] pixels2D = get2DPixels(pixels, imgWidth, imgHeight);
        int offset = (filterSize - 1) / 2;

        for (int y = offset; y < imgHeight - offset; y++) { // We keep away from the borders
            for (int x = offset; x < imgWidth - offset; x++) { // Same here for the width
                int[][] subPixels;
                if ((subPixels = getSubTable(pixels2D, x - offset, y - offset, filterSize, filterSize)) != null) {
                    // Get the average of the subrange, otherwise we keep the old non-blurred pixel
                    newPixels[y * imgWidth + x] = getAverage(subPixels);
                }
            }
        }

        result.setPixels(newPixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        Log.i("MeanBlur", "MeanBlur duration: " + elapsedTime);
        return result;
    }
}
