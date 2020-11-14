package com.idankorenisraeli.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
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

    private float barPercent;

    // region Attributes
    private float cornerRadius;
    private int barPadding;
    private int backgroundColor;
    private int colorStart;
    private int colorEnd;
    private int colorCenter;
    private int textColor;
    private ColorType colorType;



    private int textGravity; // Place the text horizontally on the bar: left/right/center/start/end
    private int textType; // Should the text show a custom string, or show current bar percentage/decimal value
    private String textString;

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

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
        try {
            cornerRadius = array.getFloat(R.styleable.CustomProgressBar_barCornerRadius, 10);
            barPadding = array.getInt(R.styleable.CustomProgressBar_barPadding, 5);
            backgroundColor = array.getColor(R.styleable.CustomProgressBar_barBackgroundColor, 0);
            barPercent = array.getFloat(R.styleable.CustomProgressBar_barPercent, 0);

            colorStart = array.getColor(R.styleable.CustomProgressBar_colorStart, 0);
            colorEnd = array.getColor(R.styleable.CustomProgressBar_colorEnd, 0);
            colorCenter = array.getColor(R.styleable.CustomProgressBar_colorCenter, 0);

            textGravity = array.getInt(R.styleable.CustomProgressBar_textGravity, 0);
            textType = array.getInt(R.styleable.CustomProgressBar_textType, 0);
            textString = array.getString(R.styleable.CustomProgressBar_textData);
        } finally {
            array.recycle();
        }


    }


    private void initBackground(Context context) {
        backgroundCard = new CardView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        backgroundCard.setLayoutParams(params);

        backgroundCard.setCardBackgroundColor(backgroundColor); // background colors from attr
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
        foregroundHolder.setWeightSum(1);

        

        foregroundCard = new CardView(context);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        cardParams.weight = barPercent;
        foregroundCard.setLayoutParams(cardParams);



        foregroundCard.setCardBackgroundColor(colorStart); // foreground colors from attr, can be changed by percent
        foregroundCard.setRadius(cornerRadius); //from attrs



        foregroundHolder.addView(foregroundCard);
    }



    private void initText(Context context)
    {
        text = new TextView(context);



        foregroundCard.addView(text);
    }




    public float getBarPercent() {
        return barPercent;
    }

    public void setBarPercent(float barPercent) {
        this.barPercent = barPercent;
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getColorStart() {
        return colorStart;
    }

    public void setColorStart(int colorStart) {
        this.colorStart = colorStart;
    }

    public int getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(int colorEnd) {
        this.colorEnd = colorEnd;
    }

    public int getColorCenter() {
        return colorCenter;
    }

    public void setColorCenter(int colorCenter) {
        this.colorCenter = colorCenter;
    }

    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
    }

    public int getTextType() {
        return textType;
    }

    public void setTextType(int textType) {
        this.textType = textType;
    }

    public ColorType getColorType() {
        return colorType;
    }

    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
