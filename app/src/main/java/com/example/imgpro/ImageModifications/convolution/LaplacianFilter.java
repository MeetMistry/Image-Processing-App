package com.example.imgpro.ImageModifications.convolution;

import android.app.Activity;
import android.graphics.Bitmap;

import com.example.imgpro.ImageModifications.AbstractImageModificationAsyncTask;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class LaplacianFilter extends AbstractImageModificationAsyncTask {
    public LaplacianFilter(Bitmap src, Activity activity) {
        super(src, activity);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap result = src.copy(Bitmap.Config.ARGB_8888, true);
        Mat mat = new Mat();
        //Turn Bitmap into Mat which is the Object type used by OpenCV
        Utils.bitmapToMat(result,mat);
        //Perform the laplacian filter using openCV method with a matrix of size 3.
        Imgproc.Laplacian(mat, mat, CvType.CV_8U, 3, 1, 0);
        //Turn Mat into Bitmap object to get the result as a Bitmap
        Utils.matToBitmap(mat,result);
        return result;
    }
}
