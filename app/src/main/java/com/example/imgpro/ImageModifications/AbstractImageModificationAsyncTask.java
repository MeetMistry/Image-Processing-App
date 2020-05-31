package com.example.imgpro.ImageModifications;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.imgpro.R;

public abstract class AbstractImageModificationAsyncTask extends AsyncTask<String,String, Bitmap> {
    protected Bitmap src;
    protected Bitmap result;
    protected Activity mActivity;

    public AbstractImageModificationAsyncTask(Bitmap src, Activity activity) {
        this.src = src;
        this.mActivity = activity;
    }

    public float ensureRange(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public int greyOutPixel(int pixel){
        int greyValue = (int) (0.299 * Color.red(pixel) + 0.587 * Color.green(pixel) + 0.114 * Color.blue(pixel));
        return Color.rgb(greyValue, greyValue, greyValue);
    }

    protected int[][] getSubTable(int[][] source, int x, int y, int width, int height) {

        if (source == null) {
            return null;
        }
        if (source.length == 0) {
            return new int[0][0];
        }
        if (height < 0) {
            throw new IllegalArgumentException("height must be positive");
        }
        if (width < 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        if ((y + height) > source.length) {
            return null;
        }
        if((x+width) > source[0].length){
            return null;
        }
        int[][] dest = new int[height][width];

        for (int destY = 0; destY < height; destY++) {
            System.arraycopy(source[y + destY], x, dest[destY], 0, width);
        }
        return dest;
    }



    @Override
    protected void onPostExecute(Bitmap result) {
        ImageView imgview = (ImageView) mActivity.findViewById(R.id.imageView);
        imgview.setImageBitmap(result);
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onProgressUpdate(String... text) {
    }
}
