package com.example.jake.hhapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jake.hhapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddImagesActivity extends ActionBarActivity {

    Button buttonCam, buttonGal;
    ImageView imageViewResult;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    static File photoFile = null;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);

        buttonCam = (Button)findViewById(R.id.buttonCam);
        buttonGal = (Button)findViewById(R.id.buttonGal);
        imageViewResult = (ImageView)findViewById(R.id.imageViewResult);


    }

    public void onClick(View v) {
        switch(v.getId())
        {
            //create intent for Android Gallery
            case R.id.buttonGal:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), REQUEST_GALLERY_PHOTO);
                break;

            //create intent for Android Camera
            case R.id.buttonCam:
                Log.d("Position", "In Button Case");
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    photoFile = createImageFile();
                    Log.d("Photofile", photoFile.toString());
                } catch (IOException ex) {
                }
                if (photoFile != null) {

                    if(!photoFile.exists()){
                        Log.d("PhotoCase", "Exists!");
                    } else {
                        Log.d("PhotoCase", "Does not exist");
                    }

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    intent.putExtra("return-data", true);
                    // start the image capture Intent
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
                break;

        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == REQUEST_GALLERY_PHOTO)
            {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = AddImagesActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Log.d("Selected Image", selectedImage.toString());

                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageViewResult.setImageBitmap(bitmap);

                } catch (Exception e){

                }

                              /*
                Intent intent = new Intent(this.getActivity(), ImageViewerActivity.class);
                intent.putExtra("image", filePath);
                startActivity(intent);*/
            }
            else if(requestCode == REQUEST_TAKE_PHOTO)
            {
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.toString());
                imageViewResult.setImageBitmap(bitmap);
                /*
                Intent intent = new Intent(this.getActivity(), ImageViewerActivity.class);
                intent.putExtra("image", photoFile.getAbsolutePath());
                startActivity(intent);*/
            }
        }
    }

    /**
     * A private method for creating an image file placeholder, so that when the camera app captures a photo, it places it
     * into and image file.
     * @return image file placeholder
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "PNG_" + "Jake-Selfie";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // Create image file
        File image = File.createTempFile(imageFileName, ".png", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("path", mCurrentPhotoPath);
        return image;
    }

}
