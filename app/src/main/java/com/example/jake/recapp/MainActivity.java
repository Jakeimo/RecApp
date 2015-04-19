package com.example.jake.recapp;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private String folder;
    private String subFolder;

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

        //Get all image names from string array in strings.xml, and put them in a String Array []
        String stringArray = getFolder() + "_" + getSubFolder() + "_array";
        Resources res = getResources();
        int id = res.getIdentifier(stringArray, "array", getPackageName());
        String[] objects = res.getStringArray(id);

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
