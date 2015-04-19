package com.example.jake.recapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private String folder;
    private String subFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //currently hardcoded, should be dynamically generated later on
        setFolder("General");
        setSubFolder("Fruit");

        String breadcrumbText = getFolder() + " > " + getSubFolder();
        TextView breadcrumb = (TextView)findViewById(R.id.breadcrumb);
        breadcrumb.setText(breadcrumbText);
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
