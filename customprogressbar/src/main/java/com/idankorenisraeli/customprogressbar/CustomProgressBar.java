package com.idankorenisraeli.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class CustomProgressBar extends FrameLayout {
    //region Inner Views
    private CardView backgroundCard;
    private CardView foregroundCard;
    private LinearLayout foregroundHolder;
    private TextView text;

    //endregion

    private float currentPercent;

    // region Attributes
    private float cornerRadius;
    private int barPadding = 20;
    private Color backgroundColor, colorStart, colorEnd, colorCenter;
    private String textString;
    private BarTextGravity textGravity;
    private BarTextType textType;
    private BarColorType colorType;
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

        addView(backgroundCard);
        addView(foregroundHolder);
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
        backgroundCard = new CardView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        backgroundCard.setLayoutParams(params);

        backgroundCard.setCardBackgroundColor(Color.parseColor("#FFFFFF")); // background colors from attr
        backgroundCard.setRadius(cornerRadius);
    }

    private void initForeground(Context context) {
        foregroundHolder = new LinearLayout(context);
        FrameLayout.LayoutParams holderParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        holderParams.setMargins(barPadding,barPadding,barPadding,barPadding);
        foregroundHolder.setLayoutParams(holderParams);
        foregroundHolder.setOrientation(LinearLayout.HORIZONTAL);
        foregroundHolder.setTranslationZ(90); //bring to front
        foregroundHolder.setBackgroundColor(Color.parseColor("#888B4B"));
        foregroundHolder.setWeightSum(1);



        foregroundCard = new CardView(context);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        cardParams.weight = 0.5f;
        foregroundCard.setLayoutParams(cardParams);



        foregroundCard.setCardBackgroundColor(Color.parseColor("#4B4B4B")); // foreground colors from attr, can be changed by percent
        foregroundCard.setRadius(cornerRadius); //from attrs


        foregroundHolder.addView(foregroundCard);
    }





    public float getCurrentPercent() {
        return currentPercent;
    }

    public void setCurrentPercent(float currentPercent) {
        this.currentPercent = currentPercent;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getBarPadding() {
        return barPadding;
    }

    public void setBarPadding(int barPadding) {
        this.barPadding = barPadding;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getColorStart() {
        return colorStart;
    }

    public void setColorStart(Color colorStart) {
        this.colorStart = colorStart;
    }

    public Color getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(Color colorEnd) {
        this.colorEnd = colorEnd;
    }

    public Color getColorCenter() {
        return colorCenter;
    }

    public void setColorCenter(Color colorCenter) {
        this.colorCenter = colorCenter;
    }

    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    public BarTextGravity getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(BarTextGravity textGravity) {
        this.textGravity = textGravity;
    }

    public BarTextType getTextType() {
        return textType;
    }

    public void setTextType(BarTextType textType) {
        this.textType = textType;
    }

    public BarColorType getColorType() {
        return colorType;
    }

    public void setColorType(BarColorType colorType) {
        this.colorType = colorType;
    }
}
