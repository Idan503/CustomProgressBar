package com.idankorenisraeli.customprogressbar;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.text.DecimalFormat;

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
    private int colorStatic, colorStart, colorCenter, colorEnd;
    private int textColor;
    private boolean textEnabled;
    private ColorType colorType;



    private TextGravity textGravity; // Place the text horizontally on the bar: left/right/center/start/end
    private TextType textType; // Should the text show a custom string, or show current bar percentage/decimal value
    private String textTitle;
    private int textPadding;
    private int textPaddingLeft;
    private int textPaddingStart;
    private int textPaddingRight;
    private int textPaddingEnd;

    //endregion

    private final static int DEFAULT_COLOR = Color.WHITE; // Will be used when user did not apply an attribute
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##"); // format of bar value display
    private final static String TAG = "CustomProgressBar";



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

            colorStart = array.getColor(R.styleable.CustomProgressBar_colorStart, -1);
            colorEnd = array.getColor(R.styleable.CustomProgressBar_colorEnd, -1);
            colorCenter = array.getColor(R.styleable.CustomProgressBar_colorCenter, -1);
            colorStatic = array.getColor(R.styleable.CustomProgressBar_colorStatic,-1);
            colorType = ColorType.values()[array.getInt(R.styleable.CustomProgressBar_colorType, 0)];

            textGravity = TextGravity.values()[array.getInt(R.styleable.CustomProgressBar_textGravity, 0)];
            textType = TextType.values()[array.getInt(R.styleable.CustomProgressBar_textType, 0)];
            textTitle = array.getString(R.styleable.CustomProgressBar_textTitle);
            textEnabled = array.getBoolean(R.styleable.CustomProgressBar_textEnabled, true);
            textColor = array.getColor(R.styleable.CustomProgressBar_textColor, 0);

            textPadding = array.getDimensionPixelSize(R.styleable.CustomProgressBar_textPadding, 0);
            textPaddingStart = array.getDimensionPixelSize(R.styleable.CustomProgressBar_textPaddingStart, 0);
            textPaddingEnd = array.getDimensionPixelSize(R.styleable.CustomProgressBar_textPaddingEnd, 0);
            textPaddingLeft = array.getDimensionPixelSize(R.styleable.CustomProgressBar_textPaddingLeft, 0);
            textPaddingRight = array.getDimensionPixelSize(R.styleable.CustomProgressBar_textPaddingRight, 0);
        } finally {
            array.recycle();
        }


    }


    //region Initiate Methods

    private void init(Context context, @Nullable AttributeSet attrs){
        obtainAttributes(context,attrs);
        initBackground(context);
        initForeground(context);
        initText(context);

        setLayoutTransition(new LayoutTransition());
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
        foregroundHolder.setLayoutTransition(new LayoutTransition());

        foregroundCard = new CardView(context);
        foregroundCard.setLayoutTransition(new LayoutTransition());

        updateColor();

        updateValue();
        foregroundHolder.addView(foregroundCard);
    }


    /**
     * Initializing the inner text view.
     * We can also choose Right/Left and not only Start/End
     * Text is always vertically aligned to center
     * @param context Current context
     */
    @SuppressLint("RtlHardcoded")
    private void initText(Context context) {
        if(!textEnabled)
            return;
        text = new TextView(context);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if(textPadding!=0)
            params.setMargins(textPadding, textPadding, textPadding, textPadding);


        if(textPaddingStart!=0 || textPaddingEnd!=0) {
            params.setMarginStart(textPaddingStart);
            params.setMarginEnd(textPaddingEnd);
        }
        else {
            if(textPaddingLeft !=0 || textPaddingEnd != 0)
                params.setMargins(textPaddingLeft, textPadding, textPaddingRight, textPadding);
        }

        text.setLayoutParams(params);

        text.setMaxLines(1);
        text.setLines(1);
        text.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        switch (textGravity){
            case START:
                text.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                break;
            case END:
                text.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
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


        updateText();


        text.setTextColor(textColor);
        text.setTranslationZ(90);

    }

    //endregion


    /**
     * Notifies to the user that he forgot to apply color attributes based on gradient/single mode.
     * Setting the color to default if no attribute is applied
     * @param gradient - current validation is for gradient attributes or single color
     */
    private void validateColorAttrs(boolean gradient) {
        if(gradient){
            if(colorStart == -1){
                Log.w(TAG, "colorStart attribute is not defined, although bar color type is gradient");
                colorStart = DEFAULT_COLOR;
            }
            if(colorEnd == -1){
                Log.w(TAG, "colorEnd attribute is not defined, although bar color type is gradient");
                colorEnd = DEFAULT_COLOR;
            }
        }
        else{
            if(colorStatic==-1){
                Log.w(TAG, "colorStatic attribute is not defined, although bar color type is static");
                colorStatic = DEFAULT_COLOR;
            }
        }
    }


    /**
     * Using colorStart, colorCenter (if applied), and colorEnd attributes
     * to generate the gradient background of the bar
     * @return GradientDrawable background with the applied radius.
     */
    private GradientDrawable createGradientBackground() {
        GradientDrawable gradient;
        if(colorCenter!=-1) {
            gradient = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{colorStart, colorCenter, colorEnd});
        }
        else
        {
            gradient = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{colorStart, colorEnd});
        }
        return gradient;
    }


    /**
     * Calculates which is the current bar color by gradient colors and current bar value
     * @return Current single dynamic color
     * @param centerColorExists Applied a center color that makes a tricolor gradient bar
     */
    private int getSingleDynamicColor(boolean centerColorExists){
        float r,g,b;
        Color startColor = Color.valueOf(colorStart);
        Color endColor = Color.valueOf(colorEnd);
        Color centerColor = Color.valueOf(colorCenter);
        if(centerColorExists){
            if(value>=0.5){
                // between center and end
                r = centerColor.red() + value * (endColor.red() - centerColor.red());
                g = centerColor.green() + value * (endColor.green() - centerColor.green());
                b = centerColor.blue() + value * (endColor.blue() - centerColor.blue());
            }
            else{
                // between start and center
                r = startColor.red() + value * (centerColor.red() - startColor.red());
                g = startColor.green() + value * (centerColor.green() - startColor.green());
                b = startColor.blue() + value * (centerColor.blue() - startColor.blue());
            }
        }
        else
        {
            //between start and end (No center)
            r = startColor.red() + value * (endColor.red() - startColor.red());
            g = startColor.green() + value * (endColor.green() - startColor.green());
            b = startColor.blue() + value * (endColor.blue() - startColor.blue());
        }

        // now r,g,b representing the single color of bar's value percentage in the gradient
        return android.graphics.Color.rgb(r,g,b);
    }


    private void updateColor(){
        switch (colorType){
            case SINGLE_STATIC:
                validateColorAttrs(false);
                foregroundCard.setCardBackgroundColor(colorStatic);
                foregroundCard.setRadius(cornerRadius);
                break;
            case SINGLE_DYNAMIC:
                boolean centerColorApplied = (colorCenter != 0);
                validateColorAttrs(true);
                foregroundCard.setCardBackgroundColor(getSingleDynamicColor(centerColorApplied));
                foregroundCard.setRadius(cornerRadius);
                break;
            case GRADIENT:
                validateColorAttrs(true);
                GradientDrawable backgroundGradient = createGradientBackground();
                backgroundGradient.setCornerRadius(cornerRadius);
                foregroundCard.setBackground(backgroundGradient);
                break;
        }

    }




    @Override
    public void invalidate() {
        super.invalidate();
        updateValue();
        updateText();
        if(colorType==ColorType.SINGLE_DYNAMIC)
            updateColor(); //Color is dynamic so we update its draw
    }

    /**
     * Updates the inner bar to fill the layout percentage by current value
     */
    private void updateValue()
    {

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        cardParams.weight = value;
        foregroundCard.setLayoutParams(cardParams);

    }

    /**
     * Setting the inner text to show the current value of the bar
     * In case of text type of decimal / percentage values
     */
    private void updateText()
    {
        if(!textEnabled || textType == TextType.STATIC)
            return;

        float roundedValue;
        switch (textType){
            case DECIMAL:
                roundedValue = Float.parseFloat(DECIMAL_FORMAT.format(value));
                String decimalString = String.format(getResources().getString(R.string.bar_text_decimal_value), roundedValue);
                text.setText(decimalString);

                break;
            case PERCENTAGE:
                roundedValue = Float.parseFloat(DECIMAL_FORMAT.format(value));
                int percentage = (int)(roundedValue * 100);
                String percentString = String.format(getResources().getString(R.string.bar_text_percent_value),percentage);
                text.setText(percentString);
                break;
            default:
                text.setText(textTitle); //setting the custom text given
                break;
        }
    }


    //region Getters & Setters
    public float getValue() {
        return value;
    }

    public void setValue(float newValue) {
        this.value = Math.max(0,Math.min(1,newValue)); // Bar value is between 0 and 1
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

    public CardView getBackgroundCard() {
        return backgroundCard;
    }

    public void setBackgroundCard(CardView backgroundCard) {
        this.backgroundCard = backgroundCard;
    }

    public CardView getForegroundCard() {
        return foregroundCard;
    }

    public void setForegroundCard(CardView foregroundCard) {
        this.foregroundCard = foregroundCard;
    }

    public LinearLayout getForegroundHolder() {
        return foregroundHolder;
    }

    public void setForegroundHolder(LinearLayout foregroundHolder) {
        this.foregroundHolder = foregroundHolder;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

    public boolean isTextEnabled() {
        return textEnabled;
    }

    public void setTextEnabled(boolean textEnabled) {
        this.textEnabled = textEnabled;
    }

    public int getTextPadding() {
        return textPadding;
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

    public int getTextPaddingLeft() {
        return textPaddingLeft;
    }

    public void setTextPaddingLeft(int textPaddingLeft) {
        this.textPaddingLeft = textPaddingLeft;
    }

    public int getTextPaddingStart() {
        return textPaddingStart;
    }

    public void setTextPaddingStart(int textPaddingStart) {
        this.textPaddingStart = textPaddingStart;
    }

    public int getTextPaddingRight() {
        return textPaddingRight;
    }

    public void setTextPaddingRight(int textPaddingRight) {
        this.textPaddingRight = textPaddingRight;
    }

    public int getTextPaddingEnd() {
        return textPaddingEnd;
    }

    public void setTextPaddingEnd(int textPaddingEnd) {
        this.textPaddingEnd = textPaddingEnd;
    }

    //endregion


    /**
     * Increase the current bar value, which is between 0 and 1
     * @param by Increase bar value by this number
     */
    public void increase(float by){
        setValue(value + by);
        invalidate();
    }

    /**
     * Increase bar while showing animation from old value to new value
     * @param by Increase bar value by this number
     * @param duration Duration of animation in ms
     */
    public void increaseAnimated(float by, int duration){
        float oldValue = value;
        setValue(oldValue+by);
        animateBar(oldValue, value,duration);
        invalidate();
    }


    /**
     * Decrease the current bar value, which is between 0 and 1
     * @param by Increase bar value by this number
     */
    public void decrease(float by){
        setValue(value-by);
        invalidate();
    }

    /**
     * Increase bar while showing animation from old value to new value
     * @param by Increase bar value by this number
     * @param duration Time of animation in ms
     */
    public void decreaseAnimated(float by, int duration){
        float oldValue = value;
        setValue(oldValue-by);
        animateBar(oldValue, value,duration);
        invalidate();
    }


    /**
     * Change bar value with animation
     * @param newValue Value to set the bar to
     * @param duration Time of animation in ms
     */
    public void setValueAnimated(float newValue, int duration){
        float oldValue = value;
        setValue(newValue);
        animateBar(oldValue, newValue,duration);
        invalidate();
    }


    /**
     * Animating the bar appearance
     * @param from Starting value
     * @param to End value
     * @param duration Time of animation in ms
     */
    private void animateBar(float from, float to, int duration){
        ValueAnimator animator = ValueAnimator.ofFloat(from, to); //fromWeight, toWeight
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ((LinearLayout.LayoutParams) foregroundCard.getLayoutParams()).weight = (float) animation.getAnimatedValue();
                foregroundCard.requestLayout();
            }
        });
        animator.start();
    }



}
