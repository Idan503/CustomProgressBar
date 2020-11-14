package com.idankorenisraeli.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class CustomProgressBar extends FrameLayout {
    //region Inner Views
    CardView background;
    CardView foreground;
    TextView text;

    //endregion

    float current_percent;

    // region Attributes
    float cornerRadius;
    //colors...

    //endregion



    public CustomProgressBar(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CustomProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CustomProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs){
        obtainAttributes(context,attrs);
        initBackground(context);
        initForeground(context);

        addView(background);
        addView(foreground);
    }


    private void obtainAttributes(Context context, AttributeSet attrs){
        if(attrs==null)
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
        try {
            cornerRadius = ta.getFloat(R.styleable.CustomProgressBar_barCornerRadius, 10);
        } finally {
            ta.recycle();
        }


    }


    private void initBackground(Context context) {
        background = new CardView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        background.setLayoutParams(params);

        background.setCardBackgroundColor(Color.parseColor("#FFFFFF")); // background colors from attr
        background.setRadius(cornerRadius);
    }

    private void initForeground(Context context) {
        foreground = new CardView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(5, 5, 5, 5);
        foreground.setLayoutParams(params);

        foreground.setCardBackgroundColor(Color.parseColor("#4B4B4B")); // foreground colors from attr, can be changed by percent
        foreground.setRadius(cornerRadius); //from attrs
    }


}
