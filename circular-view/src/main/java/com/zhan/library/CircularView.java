package com.zhan.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Zhan on 16-01-07.
 */
public class CircularView extends View {

    //Default values
    private final static int DEFAULT_BG_RADIUS = 50; //DP
    private final static int DEFAULT_BG_COLOR = R.color.black;
    private final static int DEFAULT_STROKE_WIDTH = 0; //DP
    private final static int DEFAULT_STROKE_COLOR = R.color.black;
    private final static int DEFAULT_STROKE_PADDING = 0; //DP
    private final static int DEFAULT_ICON_COLOR = R.color.white;
    private final static int DEFAULT_ICON_TOP_PADDING = 10; //DP
    private final static int DEFAULT_ICON_BOTTOM_PADDING = 10; //DP
    private final static int DEFAULT_ICON_LEFT_PADDING = 10; //DP
    private final static int DEFAULT_ICON_RIGHT_PADDING = 10; //DP
    private final static int DEFAULT_ICON = R.drawable.ic_smile;

    private Context context;
    private int circleRadius; //pixels
    private int circleColor;
    private int strokeWidth;  //pixels
    private int strokeColor;
    private int strokePadding; //pixels
    private int iconColor;
    private Drawable iconDrawable;
    private int icon;

    private int iconTopPadding;
    private int iconBottomPadding;
    private int iconLeftPadding;
    private int iconRightPadding;
    private Paint paint;

    public CircularView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public CircularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        this.context = context;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularView, 0, 0);
        try{
            circleRadius = a.getDimensionPixelSize(R.styleable.CircularView_cv_bgRadius, dpToPx(DEFAULT_BG_RADIUS));
            circleColor = a.getColor(R.styleable.CircularView_cv_bgColor, ContextCompat.getColor(this.context, DEFAULT_BG_COLOR));
            strokeWidth = a.getDimensionPixelSize(R.styleable.CircularView_cv_strokeWidth, dpToPx(DEFAULT_STROKE_WIDTH));
            strokeColor = a.getColor(R.styleable.CircularView_cv_strokeColor, ContextCompat.getColor(this.context, DEFAULT_STROKE_COLOR));
            strokePadding = a.getDimensionPixelSize(R.styleable.CircularView_cv_strokePadding, dpToPx(DEFAULT_STROKE_PADDING));
            icon = a.getResourceId(R.styleable.CircularView_cv_iconDrawable, DEFAULT_ICON);
            iconColor = a.getColor(R.styleable.CircularView_cv_iconColor, ContextCompat.getColor(this.context, DEFAULT_ICON_COLOR));
            iconTopPadding = a.getDimensionPixelSize(R.styleable.CircularView_cv_iconTopPadding, dpToPx(DEFAULT_ICON_TOP_PADDING));
            iconBottomPadding = a.getDimensionPixelSize(R.styleable.CircularView_cv_iconBottomPadding, dpToPx(DEFAULT_ICON_BOTTOM_PADDING));
            iconLeftPadding = a.getDimensionPixelSize(R.styleable.CircularView_cv_iconLeftPadding, dpToPx(DEFAULT_ICON_LEFT_PADDING));
            iconRightPadding = a.getDimensionPixelSize(R.styleable.CircularView_cv_iconRightPadding, dpToPx(DEFAULT_ICON_RIGHT_PADDING));
        }finally {
            a.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ss = ((strokeWidth + strokePadding) * 2);

        int desiredWidth = (circleRadius * 2) + ss;
        int desiredHeight = (circleRadius * 2) + ss;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) { //specific value
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) { //match parent
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else { //wrap content
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidthHalf = this.getWidth() / 2;
        int viewHeightHalf = this.getHeight() / 2;

        drawCircle(canvas, circleRadius, viewWidthHalf, viewHeightHalf);
        drawIcon(canvas);
    }

    private void drawCircle(Canvas canvas, int radius, int width, int height){
        if(Math.round(strokeWidth) > 0) { //If there's stroke defined
            //Paint the stroke
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(strokeColor);
            paint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(width, height, radius + strokePadding + (strokeWidth / 2), paint);
        }

        //Paint the inner circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(circleColor);
        canvas.drawCircle(width, height, radius, paint);
    }

    private void drawIcon(Canvas canvas){
        iconDrawable = ResourcesCompat.getDrawable(context.getResources(), icon, context.getTheme());

        if(iconDrawable != null){

            Rect bounds = canvas.getClipBounds();

            bounds.left += iconLeftPadding;
            bounds.right -= iconRightPadding;
            bounds.top += iconTopPadding;
            bounds.bottom -= iconBottomPadding;

            iconDrawable.setBounds(bounds);
            iconDrawable.mutate().setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            iconDrawable.draw(canvas);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Getters & Setters
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    // Circle
    //////////////////////////////////////////////////////

    public int getCircleRadius() {
        return pxToDp(circleRadius);
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = dpToPx(circleRadius);
        requestLayout();
        invalidate();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor( int circleColor) {
        this.circleColor = ContextCompat.getColor(getContext(), circleColor);
        invalidate();
    }

    //////////////////////////////////////////////////////
    // Stroke
    //////////////////////////////////////////////////////

    public int getStrokeWidth() {
        return pxToDp(strokeWidth);
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = dpToPx(strokeWidth);
        requestLayout();
        invalidate();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = ContextCompat.getColor(getContext(), strokeColor);
        invalidate();
    }

    public int getStrokePadding() {
        return pxToDp(strokePadding);
    }

    public void setStrokePadding(int strokePadding) {
        this.strokePadding = dpToPx(strokePadding);
        requestLayout();
        invalidate();
    }

    //////////////////////////////////////////////////////
    // Icon
    //////////////////////////////////////////////////////

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = ContextCompat.getColor(getContext(), iconColor);
        invalidate();
    }

    public int getIconResource(){
        return this.icon;
    }

    public void setIconResource(int icon){
        this.icon = icon;
        invalidate();
    }

    //////////////////////////////////////////////////////
    // Icon Paddings
    //////////////////////////////////////////////////////

    public int getIconTopPadding() {
        return pxToDp(iconTopPadding);
    }

    public void setIconTopPadding(int iconTopPadding) {
        this.iconTopPadding = dpToPx(iconTopPadding);
        invalidate();
    }

    public int getIconBottomPadding() {
        return pxToDp(iconBottomPadding);
    }

    public void setIconBottomPadding(int iconBottomPadding) {
        this.iconBottomPadding = dpToPx(iconBottomPadding);
        invalidate();
    }

    public int getIconLeftPadding() {
        return pxToDp(iconLeftPadding);
    }

    public void setIconLeftPadding(int iconLeftPadding) {
        this.iconLeftPadding = dpToPx(iconLeftPadding);
        invalidate();
    }

    public int getIconRightPadding() {
        return pxToDp(iconRightPadding);
    }

    public void setIconRightPadding(int iconRightPadding) {
        this.iconRightPadding = dpToPx(iconRightPadding);
        invalidate();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Etc
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
