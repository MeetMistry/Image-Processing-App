package com.example.imgpro.ImageModifications.convolution;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

public class GaussianBlurRS extends AbstractImageModificationAsyncTask {
    private final int filterSize;


    public GaussianBlurRS(Bitmap src, BlurValues filterSize, Activity activity) {
        super(src, activity);

        int filterSizeTemp = (filterSize.ordinal() * 5) + 3;
        this.filterSize = (int) ensureRange(filterSizeTemp, 0, 21);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected Bitmap doInBackground(String... strings) {
        long startTime = System.currentTimeMillis();

        result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        //Create renderscript
        RenderScript rs = RenderScript.create(mActivity.getApplicationContext());

        //Create allocation from Bitmap
        Allocation allocationIn = Allocation.createFromBitmap(rs, src);
        Allocation allocationOut = Allocation.createFromBitmap(rs, result);

        //Create script
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Set blur radius
        blurScript.setRadius(filterSize);

        //Set input for script
        blurScript.setInput(allocationIn);
        //Call script for output allocation
        blurScript.forEach(allocationOut);

        //Copy script result into bitmap
        allocationOut.copyTo(result);

        //Destroy everything to free memory
        allocationIn.destroy();
        allocationOut.destroy();
        blurScript.destroy();
        rs.destroy();

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        Log.i("MeanBlurRS", "MeanBlurRS Duration: " + elapsedTime);

        return result;

    }
}
