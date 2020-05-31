package com.example.imgpro.ImageModifications.Filters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

public class GreyFilter extends AbstractImageModificationAsyncTask {
    public GreyFilter(Bitmap src, Activity activity) {
        super(src, activity);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
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
                //Algorithm from Grey Filter, by intensity of each pixel
                int intensity = (oldRed + oldBlue + oldGreen) / 3;
                int newRed = intensity;
                int newBlue = intensity;
                int newGreen = intensity;

                //Applying new pixel values to newBitmap
                int newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }
}
