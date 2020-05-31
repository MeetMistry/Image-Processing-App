package com.example.imgpro.ImageModifications.Filters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

public class SepiaFilter extends AbstractImageModificationAsyncTask {
    public SepiaFilter(Bitmap src, Activity activity) {
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
                int oldAlpha = Color.alpha(oldPixel);

                //Algorithm for getting new values after calculation of filter
                //Algorithm from Sepia Filter
                int newRed = (int) (0.393 * oldRed + 0.769 * oldGreen + 0.189 * oldBlue);
                int newBlue = (int) (0.272 * oldRed + 0.534 * oldGreen + 0.131 * oldBlue);
                int newGreen = (int) (0.349 * oldRed + 0.686 * oldGreen + 0.168 * oldBlue);

                //If value is > HIGHEST_COLOR_VALUE then set value HIGHEST_COLOR_VALUE
                newRed = newRed > HIGHEST_COLOR_VALUE ? HIGHEST_COLOR_VALUE : newRed;
                newGreen = newGreen > HIGHEST_COLOR_VALUE ? HIGHEST_COLOR_VALUE : newGreen;
                newBlue = newBlue > HIGHEST_COLOR_VALUE ? HIGHEST_COLOR_VALUE : newBlue;

                //Applying new pixel values to newBitmap
                int newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }
}
