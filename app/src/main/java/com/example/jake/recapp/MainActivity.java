package com.example.jake.recapp;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private String folder;
    private String subFolder;
    private View.OnTouchListener imageTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currently hardcoded, should be dynamically generated later on e.g. user chooses folder and subfolder
        //Tested for different arrays
        setFolder("general");
        setSubFolder("fruit");

        String breadcrumbText = getFolder() + " > " + getSubFolder();
        TextView breadcrumb = (TextView)findViewById(R.id.breadcrumb);
        breadcrumb.setText(breadcrumbText);

        loadImage();

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnTouchListener(imageTouchListener);

    }



    public void loadImage(){
        //Get all image names from string array in strings.xml, and put them in a String Array []
        String stringArray = getFolder() + "_" + getSubFolder() + "_array";
        Resources res = getResources();
        int arrayId = res.getIdentifier(stringArray, "array", getPackageName());
        String[] objects = res.getStringArray(arrayId);

        //get image identifier, so it can be manipulated
        String imageLocation = getFolder() + "_" + getSubFolder() + "_" + objects[0];
        int imageId = res.getIdentifier(imageLocation, "drawable", getPackageName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize =  getSampleSize(imageId);
        options.inJustDecodeBounds = false;

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), imageId, options));

        //image.setImageResource(imageId);
        Log.d("ImageID", Integer.toString(imageId));
        Log.d("ImageLocation var", imageLocation);
    }

    public int getSampleSize(int imageId){
        int deviceHeight = 1000;
        int deviceWidth = 960;
        int sampleSize = 2;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(this.getResources(), imageId, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.d("Image Height", Integer.toString(imageHeight));
        Log.d("Image Width", Integer.toString(imageWidth));

        int newImageHeight = imageHeight, newImageWidth = imageWidth;

        //While image is larger than device, device by increments of one to get SampleSize
        while(newImageHeight >= deviceHeight || newImageWidth >=deviceWidth){
            newImageHeight = imageHeight/sampleSize;
            newImageWidth = imageWidth/sampleSize;

            sampleSize+=2;
            Log.d("New Image Height", Integer.toString(newImageHeight));
            Log.d("New Image Width", Integer.toString(newImageWidth));
        }
        Log.d("Scale Factor", Integer.toString(sampleSize));

        return sampleSize;
    }


    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getSubFolder() {
        return subFolder;
    }

    public void setSubFolder(String subFolder) {
        this.subFolder = subFolder;
    }




}
