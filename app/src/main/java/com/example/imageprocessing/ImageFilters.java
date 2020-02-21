package com.example.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ImageFilters {

    private final static int HIGHEST_COLOR_VALUE = 255;
    private final static int LOWEST_COLOR_VALUE = 0;

    //Apply Grey Filter on Image
    /*
    oldBitmap image where filter to be applied
    newBitmap - new image after filter
    */
    public static Bitmap setGreyFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

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



    //Apply Negative Filter on Image
    public static Bitmap setNegativeFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

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



    //Applying Sepia Filter on Image
    public static Bitmap setSepiaFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

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



    //Applying Green Filter on Image
    public static Bitmap setGreenFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

                //Each pixel is made from Red, Blue, Green, Alpha
                //so, getting current values of pixel
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                //Algorithm for getting new values after calculation of filter
                //Algorithm from Green Filter. set only green value, other 0
                int newRed = 0;
                int newBlue = 0;
                int newGreen = oldGreen;

                //Applying new pixel values to newBitmap
                int newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }



    //Apply Grey Bars Filter on Image
    public static Bitmap setGreyOutBarsFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

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
                int newPixel = 0;

                //Condition for bars, setting Grey values to particular pixel come in this range only
                if(i <= (imageWidth / 3) || i >= (imageWidth - imageWidth / 3)){
                    //Apply Grey
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                } else {
                    //Don't apply Grey
                    newPixel = oldPixel;
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }



    //Apply Sepia Bar Filter on Image
    public static Bitmap setSepiaOutBarsFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

                //Each pixel is made from Red, Blue, Green, Alpha
                //so, getting current values of pixel
                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                //Algorithm for getting new values after calculation of filter
                //Algorithm from Grey Filter, by intensity of each pixel
                int newRed = (int) (0.393 * oldRed + 0.769 * oldGreen + 0.189 * oldBlue);
                int newBlue = (int) (0.272 * oldRed + 0.534 * oldGreen + 0.131 * oldBlue);
                int newGreen = (int) (0.349 * oldRed + 0.686 * oldGreen + 0.168 * oldBlue);

                newRed = newRed > 255 ? 255 : newRed;
                newGreen = newGreen > 255 ? 255 : newGreen;
                newBlue = newBlue > 255 ? 255 : newBlue;

                //Applying new pixel values to newBitmap
                //condition for bars, setting SEPIA values to particular pixel comes in this range only
                int newPixel = 0;
                if(i <= (imageWidth / 3) || i >= (imageWidth - imageWidth/3)){
                    //Apply Sepia
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                } else {
                    //Don't apply Sepia
                    newPixel = oldPixel;
                }
                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }



    //Apply Grey Diagonal Filter on Image
    public static Bitmap setGreyDiagonalFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

                //Each pixel is made from Red, Blue, Green, Alpha
                //so, getting current values of pixel
                int oldRed = Color.red(oldPixel);
                int oldBlue = Color.blue(oldPixel);
                int oldGreen = Color.green(oldPixel);
                int oldAlpha = Color.alpha(oldPixel);

                //Algorithm for getting new values after calculation of filter
                //Algorithm from Grey Filter
                int intensity = (oldRed + oldBlue + oldGreen) / 3;
                int newRed = intensity;
                int newBlue = intensity;
                int newGreen = intensity;

                //Applying new pixel values to newBitmap
                //Condition for Diagonal, setting Grey values to particular pixel comes in this range
                int newPixel = 0;
                if(i < j - imageHeight / 4){
                    //Apply grey at lower
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                } else if ((i - (imageHeight / 4)) > j){
                    //Apply grey Upper
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                } else {
                    //Don't apply grey
                    newPixel = oldPixel;
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }



    //Apply Sepia Diagonal Filter on Image
    public static Bitmap setSepiaDiagonalFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

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

                newRed = newRed > 255 ? 255 : newRed;
                newGreen = newGreen > 255 ? 255 : newGreen;
                newBlue = newBlue > 255 ? 255 : newBlue;

                //Applying new pixel values to newBitmap
                //Condition for Diagonal, setting Sepia values to particular pixel comes in this range
                int newPixel = 0;
                if(i < j - imageHeight / 2){
                    //Apply sepia at Lower
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                } else if ((i - (imageHeight / 2)) > j){
                    //apply sepia upper
                    newPixel = Color.argb(oldAlpha, newRed, newGreen, newBlue);
                } else {
                    //Don't apply sepia
                    newPixel = oldPixel;
                }

                newBitmap.setPixel(i, j, newPixel);
            }
        }

        return newBitmap;
    }



    //Apply Sketch Filter on Image
    public static Bitmap setSketchFilter(Bitmap oldBitmap){

        //copying to newBitmap for manipulation
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Height and width of image
        int imageHeight = newBitmap.getHeight();
        int imageWidth = newBitmap.getWidth();

        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);

        //Traversing each pixel in Image as an 2D array
        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                //getting each pixel
                int oldPixel = oldBitmap.getPixel(i, j);

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
