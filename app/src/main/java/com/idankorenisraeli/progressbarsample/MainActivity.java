package com.idankorenisraeli.progressbarsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.idankorenisraeli.customprogressbar.CustomProgressBar;

public class MainActivity extends AppCompatActivity {

    CustomProgressBar customProgressBar;
    Button increaseBtn, decreaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();



        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgressBar.increaseAnimated(0.1f,150);
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgressBar.decreaseAnimated(0.1f, 150);
            }
        });

    }


    private void findViews(){
        customProgressBar = findViewById(R.id.main_CPB_example_bar);
        increaseBtn = findViewById(R.id.main_BTN_increase);
        decreaseBtn = findViewById(R.id.main_BTN_decrease);
    }
}