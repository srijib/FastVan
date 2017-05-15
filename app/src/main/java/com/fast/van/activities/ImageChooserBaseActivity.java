package com.fast.van.activities;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fast.van.R;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.dialogs.ImageChooserDialog;
import com.fast.van.utils.Log;
import com.google.android.gms.common.ConnectionResult;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;


import java.io.File;

/**
 * Created by Amandeep Singh Bagli on 14/10/15.
 */
public abstract class ImageChooserBaseActivity extends LocationBaseActivity implements ImageChooserDialog.ImageChooserDialogListener,ImageChooserListener {





    private ImageView imageViewThumbnail;

  //  private ImageView imageViewThumbSmall;

   // private TextView textViewFile;

    private ImageChooserManager imageChooserManager;

    //private ProgressBar pbar;

    private String filePath;

    private int chooserType;

    private boolean isActivityResultOver = false;

    private String originalFilePath;
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      Log.i(TAG, "Activity Created");
   /*     setContentView(R.layout.activity_image_chooser);

        Button buttonTakePicture = (Button) findViewById(R.id.buttonTakePicture);
        buttonTakePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        Button buttonChooseImage = (Button) findViewById(R.id.buttonChooseImage);
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        imageViewThumbnail = (ImageView) findViewById(R.id.imageViewThumb);
        imageViewThumbSmall = (ImageView) findViewById(R.id.imageViewThumbSmall);
        textViewFile = (TextView) findViewById(R.id.textViewFile);

        pbar = (ProgressBar) findViewById(R.id.progressBar);
        pbar.setVisibility(View.GONE);*/


    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
          //  pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        android.util.Log.i(TAG, "OnActivityResult");
        android.util.Log.i(TAG, "File Path : " + filePath);
        android.util.Log.i(TAG, "Chooser Type: " + chooserType);
        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
           // pbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                android.util.Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                android.util.Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                android.util.Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                // pbar.setVisibility(View.GONE);
                if (image != null) {
                    android.util.Log.i(TAG, "Chosen Image: Is not null");
                    //   textViewFile.setText(image.getFilePathOriginal()); //file path if required
                    loadImage(imageViewThumbnail, image.getFileThumbnail());
                    // loadImage(imageViewThumbSmall, image.getFileThumbnailSmall());
                } else {
                    android.util.Log.i(TAG, "Chosen Image: Is null");
                }
            }
        });
    }

    private void loadImage(ImageView iv, final String path) {


        if(iv!=null)
        Glide.with(this)
                .load(Uri.fromFile(new File(path)))
                .fitCenter()
                .into(iv);
    /*    Piccaso.with(activity)
                .load(Uri.fromFile(new File(path)))
                .fit()
                .centerInside()
                .into(iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        android.util.Log.i(TAG, "Picasso Success Loading Thumbnail - " + path);
                    }

                    @Override
                    public void onError() {
                        android.util.Log.i(TAG, "Picasso Error Loading Thumbnail Small - " + path);
                    }
                });*/
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                android.util.Log.i(TAG, "OnError: " + reason);
                // pbar.setVisibility(View.GONE);
                Toast.makeText(activity, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        android.util.Log.i(TAG, "Saving Stuff");
        android.util.Log.i(TAG, "File Path: " + filePath);
        android.util.Log.i(TAG, "Chooser Type: " + chooserType);
        outState.putBoolean("activity_result_over", isActivityResultOver);
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        outState.putString("orig", originalFilePath);
        outState.putString("thumb", thumbnailFilePath);
        outState.putString("thumbs", thumbnailSmallFilePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("activity_result_over")) {
                isActivityResultOver = savedInstanceState.getBoolean("activity_result_over");
                originalFilePath = savedInstanceState.getString("orig");
                thumbnailFilePath = savedInstanceState.getString("thumb");
                thumbnailSmallFilePath = savedInstanceState.getString("thumbs");
            }
        }
        android.util.Log.i(TAG, "Restoring Stuff");
        android.util.Log.i(TAG, "File Path: " + filePath);
        android.util.Log.i(TAG, "Chooser Type: " + chooserType);
        android.util.Log.i(TAG, "Activity Result Over: " + isActivityResultOver);
        if (isActivityResultOver) {
            populateData();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void populateData() {
        Log.i(TAG, "Populating Data");
        loadImage(imageViewThumbnail, thumbnailFilePath);
      //  loadImage(imageViewThumbSmall, thumbnailSmallFilePath);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       Log.i(TAG, "Activity Destroyed");
    }







    protected void chooseImageFrom(ImageView imageView){

        if (imageView==null)
            throw new NullPointerException("Image view can't be null");
        this.imageViewThumbnail=imageView;
        ImageChooserDialog.withActivity(this).show(ConfirmationDialogCodes.chooseCamera);
    }




    @Override
    public void fromCamera() {
        Log.e(TAG,"fromCamera");
        takePicture();
    }

    @Override
    public void fromGallery() {
        Log.e(TAG,"fromGallery");
        chooseImage();
    }
    public String getOriginalFilePath() {
        return originalFilePath;
    }

    @Override
    public void OnOkButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        Log.e(TAG,"OnOkButtonPressed:"+confirmationDialogCode.name());
    }

    @Override
    public void OnCancelButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        Log.e(TAG,"OnCancelButtonPressed:"+confirmationDialogCode.name());
    }
}
