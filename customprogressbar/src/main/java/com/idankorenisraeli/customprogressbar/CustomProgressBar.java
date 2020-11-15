package com.idankorenisraeli.customprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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

    private float value; //How much bar is full - Between 0 and 1

    // region Attributes
    private float cornerRadius;
    private int barPadding;
    private int backgroundColor;
    private int colorStart;
    private int colorEnd;
    private int colorCenter;
    private int textColor;
    private boolean textEnabled;
    private ColorType colorType;



    private TextGravity textGravity; // Place the text horizontally on the bar: left/right/center/start/end
    private TextType textType; // Should the text show a custom string, or show current bar percentage/decimal value
    private String textTitle;
    private int textPadding;

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



    /**
     * Those attributes can be set on xml file of where this view is implemented
     * Check @attrs.xml for more information
     * @param context Current context
     * @param attrs Attributes list
     */
    private void obtainAttributes(Context context, AttributeSet attrs){
        if(attrs==null)
            return;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
        try {
            cornerRadius = array.getFloat(R.styleable.CustomProgressBar_barCornerRadius, 10);
            barPadding = array.getDimensionPixelSize(R.styleable.CustomProgressBar_barPadding, 5);
            backgroundColor = array.getColor(R.styleable.CustomProgressBar_barBackgroundColor, Color.GRAY);
            value = array.getFloat(R.styleable.CustomProgressBar_value, 0);

            colorStart = array.getColor(R.styleable.CustomProgressBar_colorStart, Color.BLUE);
            colorEnd = array.getColor(R.styleable.CustomProgressBar_colorEnd, 0);
            colorCenter = array.getColor(R.styleable.CustomProgressBar_colorCenter, 0);

            textGravity = TextGravity.values()[array.getInt(R.styleable.CustomProgressBar_textGravity, 0)];
            textType = TextType.values()[array.getInt(R.styleable.CustomProgressBar_textType, 0)];
            textTitle = array.getString(R.styleable.CustomProgressBar_textTitle);
            textEnabled = array.getBoolean(R.styleable.CustomProgressBar_textEnabled, true);
            textColor = array.getColor(R.styleable.CustomProgressBar_textColor, 0);
            textPadding = array.getDimensionPixelSize(R.styleable.CustomProgressBar_textPadding, 3);
        } finally {
            array.recycle();
        }


    }



    private void init(Context context, @Nullable AttributeSet attrs){
        obtainAttributes(context,attrs);
        initBackground(context);
        initForeground(context);
        initText(context);

        addView(backgroundCard);
        addView(foregroundHolder);
        addView(text);
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
        cardParams.weight = value;
        foregroundCard.setLayoutParams(cardParams);



        foregroundCard.setCardBackgroundColor(colorStart); // foreground colors from attr, can be changed by percent
        foregroundCard.setRadius(cornerRadius); //from attrs

        foregroundHolder.addView(foregroundCard);
    }


    /**
     * Initializing the inner text view.
     * We can also choose Right/Left and not only Start/End
     * Text is always vertically aligned to center
     * @param context Current context
     */
    @SuppressLint("RtlHardcoded")
    private void initText(Context context)
    {
        if(!textEnabled)
            return;
        text = new TextView(context);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(textPadding,textPadding,textPadding,textPadding);
        text.setLayoutParams(params);

        text.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);



        switch (textGravity){
            case START:
                text.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                break;
            case END:
                text.setGravity(Gravity.END  | Gravity.CENTER_VERTICAL);
                break;
            case LEFT:
                text.setGravity(Gravity.LEFT  | Gravity.CENTER_VERTICAL);
                break;
            case RIGHT:
                text.setGravity(Gravity.RIGHT  | Gravity.CENTER_VERTICAL);
            case STICK_BAR:
                //tbd
                break;


            default:
                text.setGravity(Gravity.CENTER  | Gravity.CENTER_VERTICAL);
        }


        switch (textType){
            case DECIMAL:
                text.setText(value + "");
                break;
            case PERCENTAGE:
                int percentage = (int)(value * 100);
                text.setText(percentage + "%");
                break;
            default:
                text.setText(textTitle); //setting the custom text given
                break;
        }


        text.setTextColor(textColor);
        text.setTranslationZ(90);

    }




    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public TextGravity getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(TextGravity textGravity) {
        this.textGravity = textGravity;
    }

    public TextType getTextType() {
        return textType;
    }

    public void setTextType(TextType textType) {
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
