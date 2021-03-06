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

import com.idankorenisraeli.customprogressbar.callbacks.OnEmptyListener;
import com.idankorenisraeli.customprogressbar.callbacks.OnFullListener;
import com.idankorenisraeli.customprogressbar.enums.ColorType;
import com.idankorenisraeli.customprogressbar.enums.TextGravity;
import com.idankorenisraeli.customprogressbar.enums.TextType;

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
    private ColorType colorType = ColorType.SINGLE_STATIC;



    private TextGravity textGravity = TextGravity.CENTER; // Place the text horizontally on the bar: left/right/center/start/end
    private TextType textType = TextType.STATIC; // Should the text show a custom string, or show current bar percentage/decimal value
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

    private OnFullListener onFullListener = null;
    private OnEmptyListener onEmptyListener = null;



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
    private void obtainAttributes(Context context,@Nullable AttributeSet attrs){
        if(attrs==null) {
            // No attributes given - default initialize
            colorStart = -1;
            colorCenter = -1;
            colorEnd = -1;
            textType = TextType.STATIC;
            colorType = ColorType.SINGLE_STATIC;
            textGravity = TextGravity.CENTER;
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
        try {
            cornerRadius = array.getDimensionPixelSize(R.styleable.CustomProgressBar_barCornerRadius, 4);
            barPadding = array.getDimensionPixelSize(R.styleable.CustomProgressBar_barPadding, 3);
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
        if(text!=null)
            addView(text);
    }


    private void initBackground(Context context) {
        backgroundCard = new CardView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        backgroundCard.setLayoutParams(params);

        updateBackground();
    }


    private void initForeground(Context context) {
        foregroundHolder = new LinearLayout(context);

        foregroundHolder.setOrientation(LinearLayout.HORIZONTAL);
        foregroundHolder.setTranslationZ(90); //bring to front
        foregroundHolder.setWeightSum(1);

        foregroundCard = new CardView(context);

        updateForeground();
        foregroundHolder.addView(foregroundCard);
    }


    /**
     * Initializing the inner text view.
     * We can also choose Right/Left and not only Start/End
     * Text is always vertically aligned to center
     * @param context Current context
     */
    private void initText(Context context) {
        text = new TextView(context);


        text.setMaxLines(1);
        text.setLines(1);
        text.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);



        updateText();



        text.setTranslationZ(90);

    }

    //endregion


    /**
     * Notifies to the user that he forgot to apply color attributes based on gradient/single mode.
     * @param gradient - current validation is for gradient attributes or single color
     */
    private void validateColorAttrs(boolean gradient) {
        if(gradient){
            if(colorStart == -1){
                Log.w(TAG, "colorStart attribute is not defined, although bar color type is gradient");
            }
            if(colorEnd == -1){
                Log.w(TAG, "colorEnd attribute is not defined, although bar color type is gradient");
            }
            // NOTE: colorCenter can be not set, which makes its value to be -1, and the gradient will be 2 color only.
        }
        else{
            if(colorStatic==-1){
                Log.w(TAG, "colorStatic attribute is not defined, although bar color type is static");
            }
        }
    }


    /**
     * Using colorStart, colorCenter (if applied), and colorEnd attributes
     * to generate the gradient background of the bar
     * @return GradientDrawable background with the applied radius.
     * @param colors Colors of the background, multiple for gradient.
     */
    private GradientDrawable createBarBackground(int... colors) {
        if(colors.length==0)
            return null;
        GradientDrawable background;
        if(colors.length==1){
            background = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{colors[0], colors[0]});
        }
        else{
            // 2+ parameters - regular gradient
            background = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    colors);
        }

        background.setCornerRadius(cornerRadius);
        return background;
    }



    /**
     * Calculates which is the current bar color by gradient colors and current bar value
     * @return Current single dynamic color
     */
    private int getSingleDynamicColor(){
        float r,g,b; //result
        Color start, center, end;
        boolean centerColorExists = (colorCenter!=-1);

        //Setting color to default if there is no color
        if(colorStart==-1)
            start = Color.valueOf(DEFAULT_COLOR);
        else
            start = Color.valueOf(colorStart);

        if (colorCenter == -1)
            center = Color.valueOf(DEFAULT_COLOR);
        else
            center = Color.valueOf(colorCenter);

        if(colorEnd==-1)
            end = Color.valueOf(DEFAULT_COLOR);
        else
            end = Color.valueOf(colorEnd);
        
        if(centerColorExists){
            if(value>=0.5){
                // between center and end - value is in (0.5,1), converted to range (0,1)
                r = center.red() + (value-0.5f)*2 * (end.red() - center.red());
                g = center.green() + (value-0.5f)*2 * (end.green() - center.green());
                b = center.blue() + (value-0.5f)*2 * (end.blue() - center.blue());
            }
            else{
                // between start and center - value is in (0,0.5), converted to range (0,1)
                r = start.red() + (value*2) * (center.red() - start.red());
                g = start.green() + (value*2) * (center.green() - start.green());
                b = start.blue() + (value*2) * (center.blue() - start.blue());
            }
        }
        else
        {
            //between start and end (No center color)
            r = start.red() + value * (end.red() - start.red());
            g = start.green() + value * (end.green() - start.green());
            b = start.blue() + value * (end.blue() - start.blue());
        }

        // now r,g,b representing the single color of bar's value percentage in the gradient
        return android.graphics.Color.rgb(r,g,b);
    }


    @Override
    public void invalidate() {
        super.invalidate();
        updateBackground();
        updateForeground();
        updateText();
    }


    //region Update UI Methods

    private void updateColor(){
        switch (colorType){
            case SINGLE_STATIC:
                validateColorAttrs(false);
                foregroundCard.setBackground(createBarBackground(colorStatic));
                break;
            case SINGLE_DYNAMIC:
                validateColorAttrs(true);
                foregroundCard.setBackground(createBarBackground(getSingleDynamicColor()));
                break;
            case GRADIENT:
                validateColorAttrs(true);
                if(colorCenter!=-1) {
                    // Center color applied, gradient includes 3 colors
                    foregroundCard.setBackground(createBarBackground(colorStart, colorCenter, colorEnd));
                }
                else {
                    // Only 2 colors applied
                    foregroundCard.setBackground(createBarBackground(colorStart, colorEnd));
                }
                break;
        }

    }

    private void updateBackground(){
        backgroundCard.setCardBackgroundColor(backgroundColor); // background colors from attr
        backgroundCard.setRadius(cornerRadius);
    }


    private void updateForeground(){
        FrameLayout.LayoutParams holderParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        holderParams.setMargins(barPadding,barPadding,barPadding,barPadding);

        foregroundHolder.setLayoutParams(holderParams);

        updateColor();
        updateValue();
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
    @SuppressLint("RtlHardcoded")
    private void updateText()
    {
        if(!textEnabled) {
            text.setVisibility(GONE);
            return; //Text will not be shown anyway
        }
        else
            text.setVisibility(VISIBLE);

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

        // Update padding
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if(textPadding!=0)
            params.setMargins(textPadding, textPadding, textPadding, textPadding);


        if(textPaddingStart!=0 || textPaddingEnd!=0) {
            params.setMarginStart(textPaddingStart);
            params.setMarginEnd(textPaddingEnd);
        }
        else {
            if(textPaddingLeft !=0 || textPaddingRight != 0)
                params.setMargins(textPaddingLeft, textPadding, textPaddingRight, textPadding);
        }

        text.setLayoutParams(params);
        text.setTextColor(textColor);
    }

    //endregion


    //region Getters & Setters
    public float getValue() {
        return value;
    }

    /**
     * Bar value is always between 0.00 and 1.00.
     * @param newValue Value that the bar will be set to, in range [0,1].
     */
    public void setValue(float newValue) {
        this.value = Math.max(0,Math.min(1,newValue)); // Bar value is between 0 and 1

        if(value==1 && onFullListener !=null)
            onFullListener.onBarFull();
        if(value==0 && onEmptyListener !=null)
            onEmptyListener.onBarEmpty();


        invalidate();
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    public int getBarPadding() {
        return barPadding;
    }

    public void setBarPadding(int barPadding) {
        this.barPadding = barPadding;
        invalidate();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }

    public int getColorStart() {
        return colorStart;
    }

    public void setColorStart(int colorStart) {
        this.colorStart = colorStart;
        invalidate();
    }

    public int getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(int colorEnd) {
        this.colorEnd = colorEnd;
        invalidate();
    }

    public int getColorCenter() {
        return colorCenter;
    }

    public void setColorCenter(int colorCenter) {
        this.colorCenter = colorCenter;
        invalidate();
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
        if(!textEnabled) {
            textEnabled = true;
            Log.w(TAG, "A new title is set, 'textEnabled' attribute was turned on automatically");
        }
        invalidate(); //Update the view to show the new text
    }

    public TextGravity getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(TextGravity textGravity) {
        this.textGravity = textGravity;
        invalidate();
    }

    public TextType getTextType() {
        return textType;
    }

    public void setTextType(TextType textType) {
        this.textType = textType;
        invalidate();
    }

    public ColorType getColorType() {
        return colorType;
    }

    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public CardView getBackgroundCard() {
        return backgroundCard;
    }

    private void setBackgroundCard(CardView backgroundCard) {
        this.backgroundCard = backgroundCard;
        invalidate();
    }




    public boolean isTextEnabled() {
        return textEnabled;
    }

    public void setTextEnabled(boolean textEnabled) {
        this.textEnabled = textEnabled;
        invalidate();
    }

    public int getTextPadding() {
        return textPadding;
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
        invalidate();
    }

    public int getTextPaddingLeft() {
        return textPaddingLeft;
    }

    public void setTextPaddingLeft(int textPaddingLeft) {
        this.textPaddingLeft = textPaddingLeft;
        invalidate();
    }

    public int getTextPaddingStart() {
        return textPaddingStart;
    }

    public void setTextPaddingStart(int textPaddingStart) {
        this.textPaddingStart = textPaddingStart;
        invalidate();
    }

    public int getTextPaddingRight() {
        return textPaddingRight;
    }

    public void setTextPaddingRight(int textPaddingRight) {
        this.textPaddingRight = textPaddingRight;
        invalidate();
    }

    public int getTextPaddingEnd() {
        return textPaddingEnd;
    }

    public void setTextPaddingEnd(int textPaddingEnd) {
        this.textPaddingEnd = textPaddingEnd;
        invalidate();
    }

    public int getColorStatic() {
        return colorStatic;
    }

    public void setColorStatic(int colorStatic) {
        this.colorStatic = colorStatic;
        invalidate();
    }

    public OnFullListener getOnFullListener() {
        return onFullListener;
    }

    /**
     * @param onFullListener Callback for when bar reaches 1
     */
    public void setOnFullListener(OnFullListener onFullListener) {
        this.onFullListener = onFullListener;
    }

    public OnEmptyListener getOnEmptyListener() {
        return onEmptyListener;
    }

    /**
     * @param onEmptyListener Callback for when bar reaches 0
     */
    public void setOnEmptyListener(OnEmptyListener onEmptyListener) {
        this.onEmptyListener = onEmptyListener;
    }

    //endregion


    /**
     * Increase the current bar value, which is between 0 and 1
     * @param by Increase bar value by this number
     */
    public void increase(float by){
        setValue(value + by);
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
    }


    /**
     * Decrease the current bar value, which is between 0 and 1
     * @param by Increase bar value by this number
     */
    public void decrease(float by){
        setValue(value-by);
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
