package com.example.imgpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.imgpro.ImageModifications.Filters.GreenFilter;
import com.example.imgpro.ImageModifications.Filters.GreyDiagonal;
import com.example.imgpro.ImageModifications.Filters.GreyFilter;
import com.example.imgpro.ImageModifications.Filters.GreyOutBarFilter;
import com.example.imgpro.ImageModifications.Filters.NegativeFilter;
import com.example.imgpro.ImageModifications.Filters.SepiaBarOutFilter;
import com.example.imgpro.ImageModifications.Filters.SepiaDiagonal;
import com.example.imgpro.ImageModifications.Filters.SepiaFilter;
import com.example.imgpro.ImageModifications.Filters.SketchFilter;
import com.example.imgpro.ImageModifications.convolution.BlurValues;
import com.example.imgpro.ImageModifications.convolution.GaussianBlurRS;
import com.example.imgpro.ImageModifications.convolution.HistogramEqualizationOCV;
import com.example.imgpro.ImageModifications.convolution.LaplacianFilter;
import com.example.imgpro.ImageModifications.convolution.MeanBlurRS;
import com.example.imgpro.ImageModifications.convolution.SobelFilter;
import com.google.android.material.snackbar.Snackbar;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button cameraBtn, galleryBtn, saveBtn;
    BitmapDrawable drawable;
    Bitmap bitmap;
    private static final int CAMERA_PERM_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int GALLERY_REQUEST_CODE = 3;
    private static final String TAG = "MainActivityLog";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public ZoomAndScrollImageView mainImageView;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private LinearLayout menuPicker;
    private LinearLayout filterOptions;
    private String pictureImagePath = "";
    private LinearLayout currentActiveFiltersMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveBtn = findViewById(R.id.saveButton);
        galleryBtn = findViewById(R.id.loadFromGalleryButton);
        cameraBtn = findViewById(R.id.loadFromCameraButton);
        mainImageView = (ZoomAndScrollImageView) findViewById(R.id.imageView);
        menuPicker = (LinearLayout) findViewById(R.id.menuPickerLinearLayout);
        filterOptions = (LinearLayout) findViewById(R.id.filterOptionsLinearLayout);
        Button undoButton = (Button) findViewById(R.id.undoButton);

        // The onLongClick event isn't available in XML, so we define it here.
        if (undoButton != null) {
            undoButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mainImageView.resetPicture();
                    return true;
                }
            });
        }


        Bitmap basedImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.car);
        // Adding the image programmatically so it gets added to the ZoomAndScrollImageView stack
        mainImageView.setImageBitmap(basedImage);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable = (BitmapDrawable) mainImageView.getDrawable();
                bitmap = drawable.getBitmap();

                FileOutputStream outputStream = null;

                File sdCard = new File(Environment.DIRECTORY_PICTURES);
                File directory = new File(sdCard.getAbsolutePath());
                directory.mkdir();
                String fileName = String.format("%d.jpg",System.currentTimeMillis());
                File outFile = new File(directory,fileName);

                Toast.makeText(MainActivity.this,"Image saved",Toast.LENGTH_SHORT).show();

                try {
                    outputStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    sendBroadcast(intent);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE) {
            if(grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }else {
                Toast.makeText(this, "Camera Permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            mainImageView.setImageBitmap(image);
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag","OnActivityResult: Gallery Image Uri: " + imageFileName);
                mainImageView.setImageURI(contentUri);
            }
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearFilterOptions();
        onMenuBackClicked(null);
    }

    public void onMenuBackClicked(View view) {
        clearFilterOptions();
        if (currentActiveFiltersMenu != null) {
            currentActiveFiltersMenu.setVisibility(View.GONE);
            menuPicker.setVisibility(View.VISIBLE);
        }
    }

    public void onCategoriesMenuButtonClicked(View view) {
        menuPicker.setVisibility(View.INVISIBLE);
        switch (view.getId()) {
            case R.id.filterButton:
                LinearLayout filterLinearLayout = (LinearLayout) findViewById(R.id.filterLinearLayout);
                if (filterLinearLayout != null) {
                    filterLinearLayout.setVisibility(View.VISIBLE);
                    currentActiveFiltersMenu = filterLinearLayout;
                }
                break;
            case R.id.convolutionButton:
                LinearLayout convolutionLinearLayout = (LinearLayout) findViewById(R.id.convolutionLinearLayout);
                if (convolutionLinearLayout != null) {
                    convolutionLinearLayout.setVisibility(View.VISIBLE);
                    currentActiveFiltersMenu = convolutionLinearLayout;
                }
                break;
        }
    }

    private Bitmap getImageViewBitmap() {
        if (mainImageView != null) {
            return ((BitmapDrawable) mainImageView.getDrawable()).getBitmap();
        }
        return null;
    }

    private void clearFilterOptions() {

        if (filterOptions != null) {
            filterOptions.removeAllViews();
        }
    }

    public void onGreyButtonClicked(View view) {
        clearFilterOptions();

        GreyFilter greyFilter = new GreyFilter(getImageViewBitmap(), this);
        greyFilter.execute();
    }

    public void onNegativeButtonClicked(View view) {
        clearFilterOptions();

        NegativeFilter negativeFilter = new NegativeFilter(getImageViewBitmap(), this);
        negativeFilter.execute();
    }

    public void onSepiaButtonClicked(View view) {
        clearFilterOptions();

        SepiaFilter sepiaFilter = new SepiaFilter(getImageViewBitmap(), this);
        sepiaFilter.execute();
    }

    public void onGreenButtonClicked(View view) {
        clearFilterOptions();

        GreenFilter greenFilter = new GreenFilter(getImageViewBitmap(), this);
        greenFilter.execute();
    }

    public void onSepiaOutBarButtonClicked(View view) {
        clearFilterOptions();

        SepiaBarOutFilter sepiaBarOutFilter = new SepiaBarOutFilter(getImageViewBitmap(), this);
        sepiaBarOutFilter.execute();
    }

    public void onGreyDiagonalButtonClicked(View view) {
        clearFilterOptions();

        GreyDiagonal greyDiagonal = new GreyDiagonal(getImageViewBitmap(), this);
        greyDiagonal.execute();
    }

    public void onSepiaDiagonalButtonClicked(View view) {
        clearFilterOptions();

        SepiaDiagonal sepiaDiagonal = new SepiaDiagonal(getImageViewBitmap(), this);
        sepiaDiagonal.execute();
    }

    public void onSketchButtonClicked(View view) {
        clearFilterOptions();

        SketchFilter sketchFilter = new SketchFilter(getImageViewBitmap(), this);
        sketchFilter.execute();
    }

    public void onGreyOutBarButtonClicked(View view) {
        clearFilterOptions();

        GreyOutBarFilter greyOutBarFilter = new GreyOutBarFilter(getImageViewBitmap(), this);
        greyOutBarFilter.execute();
    }

    public void onHistoEqButtonClicked(View view) {
        clearFilterOptions();

        HistogramEqualizationOCV histogramEqualization = new HistogramEqualizationOCV(getImageViewBitmap(), this);
        histogramEqualization.execute();
    }



    public void onGaussianBlurButtonClicked(View view) {
        clearFilterOptions();
        Button min = new Button(this);
        min.setText(R.string.minimum_value);
        Button med = new Button(this);
        med.setText(R.string.medium_value);
        Button max = new Button(this);
        max.setText(R.string.maximum_value);


        View.OnClickListener optionButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                BlurValues value = BlurValues.MIN;
                if (b.getText().toString().compareTo(String.valueOf(R.string.minimum_value)) == 0) {
                    value = BlurValues.MIN;
                }
                if (b.getText().toString().compareTo(String.valueOf(R.string.medium_value)) == 0) {
                    value = BlurValues.MED;
                }
                if (b.getText().toString().compareTo(String.valueOf(R.string.maximum_value)) == 0) {
                    value = BlurValues.MAX;
                }
                GaussianBlurRS gaussianBlur = new GaussianBlurRS(getImageViewBitmap(), value, MainActivity.this);
                gaussianBlur.execute();
            }
        };

        min.setOnClickListener(optionButtonClick);
        med.setOnClickListener(optionButtonClick);
        max.setOnClickListener(optionButtonClick);

        if (filterOptions != null) {
            filterOptions.addView(min);
            filterOptions.addView(med);
            filterOptions.addView(max);
        }
    }


    public void onMeanBlurButtonClicked(View view) {
        clearFilterOptions();
        Button min = new Button(this);
        min.setText(R.string.minimum_value);
        Button max = new Button(this);
        max.setText(R.string.maximum_value);


        View.OnClickListener optionButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                BlurValues value = BlurValues.MIN;
                if (b.getText().toString().compareTo(String.valueOf(R.string.minimum_value)) == 0) {
                    value = BlurValues.MIN;
                }
                if (b.getText().toString().compareTo(String.valueOf(R.string.maximum_value)) == 0) {
                    value = BlurValues.MAX;
                }

                MeanBlurRS meanBlur = new MeanBlurRS(getImageViewBitmap(), value, MainActivity.this);
                meanBlur.execute();

            }
        };

        min.setOnClickListener(optionButtonClick);
        max.setOnClickListener(optionButtonClick);

        if (filterOptions != null) {
            filterOptions.addView(min);
            filterOptions.addView(max);
        }

    }

    public void onLaplacianButtonClicked(View view) {
        clearFilterOptions();
        LaplacianFilter laplacianFilter = new LaplacianFilter(getImageViewBitmap(), this);
        laplacianFilter.execute();
    }


    public void onSobelButtonClicked(View view) {
        clearFilterOptions();

        SobelFilter asynctask = new SobelFilter(getImageViewBitmap(), this);
        asynctask.execute();

    }


    public void onUndoButtonClicked(View view) {
        mainImageView.undoModification();
    }
}
