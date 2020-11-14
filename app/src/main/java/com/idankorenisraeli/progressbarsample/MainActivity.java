package com.idankorenisraeli.progressbarsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.idankorenisraeli.customprogressbar.CustomProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomProgressBar progressBar = new CustomProgressBar(this);
    }
}