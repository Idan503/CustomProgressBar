package com.idankorenisraeli.progressbarsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.idankorenisraeli.customprogressbar.ColorType;
import com.idankorenisraeli.customprogressbar.CustomProgressBar;
import com.idankorenisraeli.customprogressbar.TextGravity;
import com.idankorenisraeli.customprogressbar.TextType;

public class MainActivity extends AppCompatActivity {

    CustomProgressBar customProgressBar;
    Button increaseBtn, decreaseBtn;
    Button increaseAnimBtn, decreaseAnimBtn;

    RadioGroup textTypeGroup, textGravityGroup, colorTypeGroup;

    private static final int ANIMATION_DURATION = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        setValueButtonsListeners();
        setOptionsButtonsListeners();




    }

    private void setValueButtonsListeners(){
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgressBar.increase(0.1f);
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgressBar.decrease(0.1f);
            }
        });

        increaseAnimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgressBar.increaseAnimated(0.1f,ANIMATION_DURATION);
            }
        });

        decreaseAnimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgressBar.decreaseAnimated(0.1f,ANIMATION_DURATION);
            }
        });
    }

    private void setOptionsButtonsListeners(){
        textTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.main_RTB_percentage){
                    customProgressBar.setTextType(TextType.PERCENTAGE);
                }
                else
                if(checkedId == R.id.main_RTB_decimal){
                    customProgressBar.setTextType(TextType.DECIMAL);
                }
                else
                if(checkedId == R.id.main_RTB_custom){
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                    EditText titleEditText = new EditText(MainActivity.this);
                    dialogBuilder.setTitle("Set a Custom Title");
                    titleEditText.setPadding(15,15,15,15);
                    titleEditText.setHint("Custom Static Title");
                    dialogBuilder.setView(titleEditText);
                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!titleEditText.getText().toString().equals("")) {
                                customProgressBar.setTextType(TextType.STATIC);
                                customProgressBar.setTextTitle(titleEditText.getText().toString());
                            }
                        };
                    });
                    dialogBuilder.setNegativeButton("Cancel", null);
                    dialogBuilder.show();

                }

            }
        });


        textGravityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.main_RTB_start){
                    customProgressBar.setTextGravity(TextGravity.START);
                }
                else
                if(checkedId == R.id.main_RTB_center){
                    customProgressBar.setTextGravity(TextGravity.CENTER);
                }
                else
                if(checkedId == R.id.main_RTB_end){
                    customProgressBar.setTextGravity(TextGravity.END);
                }
            }
        });

        colorTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.main_RTB_single_static){
                    customProgressBar.setColorType(ColorType.SINGLE_STATIC);
                }
                else
                if(checkedId == R.id.main_RTB_single_dynamic){
                    customProgressBar.setColorType(ColorType.SINGLE_DYNAMIC);
                }
                else
                if(checkedId == R.id.main_RTB_gradient){
                    customProgressBar.setColorType(ColorType.GRADIENT);
                }
            }
        });
    }


    private void findViews(){
        customProgressBar = findViewById(R.id.main_CPB_example_bar);
        increaseBtn = findViewById(R.id.main_BTN_increase);
        decreaseBtn = findViewById(R.id.main_BTN_decrease);
        increaseAnimBtn = findViewById(R.id.main_BTN_increase_animated);
        decreaseAnimBtn = findViewById(R.id.main_BTN_decrease_animated);
        textGravityGroup = findViewById(R.id.main_RBG_text_gravity);
        textTypeGroup = findViewById(R.id.main_RBG_text_type);
        colorTypeGroup = findViewById(R.id.main_RBG_color_type);

    }
}