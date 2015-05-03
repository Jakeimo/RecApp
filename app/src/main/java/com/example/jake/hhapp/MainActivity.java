package com.example.jake.hhapp;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends ActionBarActivity {

    private String folder;
    private String subFolder;
    private String currentImage;
    private int previousResult=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currently hardcoded, should be dynamically generated later on e.g. user chooses folder and subfolder
        //Tested for different arrays
        setFolder("general");
        setSubFolder("fruit");

        //Set breadcrumb text_expand
        String breadcrumbText = getFolder() + " > " + getSubFolder();
        TextView breadcrumb = (TextView)findViewById(R.id.breadcrumb);
        breadcrumb.setText(breadcrumbText);

        //Load first image
        try{
            loadImage();
        }catch (Exception e){}

        //Load click listener to image, which adds text_expand
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(imageClickListener);

    }
    public View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                loadText();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                loadSound();
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Load image after animation is finished
                        clearText();
                        loadImage();
                    }
                }, 3000);

        }
    };

    private void loadSound() {
        String image = getCurrentImage().split("_")[2];

        Resources res = getResources();
        int fileId = res.getIdentifier(image, "raw", getPackageName());
        Log.d("Current image", image);
        Log.d("fileId", Integer.toString(fileId));
        try {
            MediaPlayer mp = MediaPlayer.create(this, fileId);
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        Log.d("Audio", "Played");

    }

    public void clearText() {
        TextView textView = (TextView) findViewById(R.id.ImageText);
        textView.setText("");
    }

    /*
    * This method gets the current image name, splits it to get the specific image (as opposed to
    * it's folder and subfolder), then sets that text_expand to the TextView, and applies animation
    * */
    public void loadText(){
        String image = getCurrentImage();

        //Split image into an array of folder-subfolder-name then capitalise name
        String[] imageArray = image.split("_");
        String imageName = imageArray[2];
        //Gets the first letter, capitalises then combines with the rest of the lowercase string
        String finalImageText = imageName.substring(0,1).toUpperCase() + imageName.substring(1);

        //set associated image text_expand
        TextView textView = (TextView) findViewById(R.id.ImageText);
        textView.setText(finalImageText);

        //Load animation and apply it to Text
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.text_expand);
        textView.startAnimation(anim);
    }

    public void loadImage(){
        //Get all image names from string array in strings.xml, and put them in a String Array []
        String stringArray = getFolder() + "_" + getSubFolder() + "_array";
        Resources res = getResources();
        int arrayId = res.getIdentifier(stringArray, "array", getPackageName());
        String[] objects = res.getStringArray(arrayId);

        int result;
            do{
                Random rnd = new Random();
                rnd.setSeed(System.currentTimeMillis());
                result = rnd.nextInt(3);
            } while( result == getPreviousResult());

        setPreviousResult(result);

        //get image identifier, so it can be manipulated, and set image name
        String imageLocation = getFolder() + "_" + getSubFolder() + "_" + objects[result];
        int imageId = res.getIdentifier(imageLocation, "drawable", getPackageName());
        setCurrentImage(res.getResourceEntryName(imageId));

        //Sets sample size to reduce quality of image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize =  getSampleSize(imageId);
        options.inJustDecodeBounds = false;

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), imageId, options));
    }

    /*
    * Uses fixed var's for height and width of ImageView.
    * If image is larger than these fixed variables, then it will be halved
    *
    * Returns: how many times the image needs to be halved (i.e. 1/2 1/4 1/6 etc)
    * */
    public int getSampleSize(int imageId){
        int deviceHeight = 1000;
        int deviceWidth = 960;
        int sampleSize = 2;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(this.getResources(), imageId, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        int newImageHeight = imageHeight, newImageWidth = imageWidth;

        //While image is larger than device, device by increments of one to get SampleSize
        while(newImageHeight >= deviceHeight || newImageWidth >=deviceWidth){
            newImageHeight = imageHeight/sampleSize;
            newImageWidth = imageWidth/sampleSize;

            sampleSize+=2;
        }

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


    public String getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(String currentImage) {
        this.currentImage = currentImage;
    }

    public int getPreviousResult() {
        return previousResult;
    }

    public void setPreviousResult(int previousResult) {
        this.previousResult = previousResult;
    }
}
