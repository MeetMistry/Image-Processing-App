package com.example.imgpro.ImageModifications.Filters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

public class SketchFilter extends AbstractImageModificationAsyncTask {
    public SketchFilter(Bitmap src, Activity activity) {
        super(src, activity);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        final int HIGHEST_COLOR_VALUE = 255;
        final int LOWEST_COLOR_VALUE = 0;
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
                int oldAlpha = Color.alpha(oldPixel);

                //Algorithm for getting new values after calculation of filter
                //Algorithm from Sketch Filter
                int intensity = (oldRed + oldBlue + oldGreen) / 3;

                //Applying new pixel values to newBitmap
                //Condition for Sketch
                int newPixel = 0;
                int INTENSITY_FACTOR = 120;

                if(intensity > INTENSITY_FACTOR){
                    //Apply white color
                    newPixel = Color.argb(oldAlpha, HIGHEST_COLOR_VALUE, HIGHEST_COLOR_VALUE, HIGHEST_COLOR_VALUE);
                } else if (intensity > 100){
                    //apply grey color
                    newPixel = Color.argb(oldAlpha, 150, 150, 150);
                } else {
                    //Apply black Color
                    newPixel = Color.argb(oldAlpha, LOWEST_COLOR_VALUE, LOWEST_COLOR_VALUE, LOWEST_COLOR_VALUE);
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }
}
