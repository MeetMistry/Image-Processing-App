package com.example.imgpro.ImageModifications.Filters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

public class NegativeFilter extends AbstractImageModificationAsyncTask {

    public NegativeFilter(Bitmap src, Activity activity) {
        super(src, activity);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        final int HIGHEST_COLOR_VALUE = 255;
        Bitmap newBitmap = this.src.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = this.src.getPixel(i, j);

                //Each pixel is made from Red, Blue, Green, Alpha
                //so, getting current values of pixel
                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);

                //Algorithm for getting new values after calculation of filter
                //Algorithm from Negative Filter
                int newRed = HIGHEST_COLOR_VALUE - oldRed;
                int newBlue = HIGHEST_COLOR_VALUE - oldBlue;
                int newGreen = HIGHEST_COLOR_VALUE - oldGreen;

                //Applying new pixel values to newBitmap
                int newPixel = Color.rgb(newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }
}
