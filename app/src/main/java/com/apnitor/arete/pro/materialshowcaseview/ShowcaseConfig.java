package com.apnitor.arete.pro.materialshowcaseview;

import android.graphics.Color;
import android.graphics.Typeface;

import com.apnitor.arete.pro.materialshowcaseview.shape.CircleShape;
import com.apnitor.arete.pro.materialshowcaseview.shape.Shape;


public class ShowcaseConfig {

    public static final String DEFAULT_MASK_COLOUR = "#dd335075";
    public static final long DEFAULT_FADE_TIME = 300;
    public static final long DEFAULT_DELAY = 0;
    public static final Shape DEFAULT_SHAPE = new CircleShape();
    public static final int DEFAULT_SHAPE_PADDING = 10;

    private long mDelay = DEFAULT_DELAY;
    private int mMaskColour;
    private Typeface mDismissTextStyle = Typeface.DEFAULT_BOLD;

    private int mContentTextColor;
    private int mDismissTextColor;
    private long mFadeDuration = DEFAULT_FADE_TIME;
    private Shape mShape = DEFAULT_SHAPE;
    private int mShapePadding = DEFAULT_SHAPE_PADDING;
    private boolean renderOverNav = false;

    public ShowcaseConfig() {
        mMaskColour = Color.parseColor(ShowcaseConfig.DEFAULT_MASK_COLOUR);
        mContentTextColor = Color.parseColor("#000000");
        mDismissTextColor = Color.parseColor("#FFFFFF");
    }

    public long getDelay() {
        return mDelay;
    }

    public void setDelay(long delay) {
        this.mDelay = delay;
    }

    public int getMaskColor() {
        return mMaskColour;
    }

    public void setMaskColor(int maskColor) {
        mMaskColour = maskColor;
    }

    public int getContentTextColor() {
        return mContentTextColor;
    }

    public void setContentTextColor(int mContentTextColor) {
        this.mContentTextColor = mContentTextColor;
    }

    public int getDismissTextColor() {
        return mDismissTextColor;
    }

    public void setDismissTextColor(int dismissTextColor) {
        this.mDismissTextColor = dismissTextColor;
    }

    public Typeface getDismissTextStyle() {
        return mDismissTextStyle;
    }

    public void setDismissTextStyle(Typeface dismissTextStyle) {
        this.mDismissTextStyle = dismissTextStyle;
    }

    public long getFadeDuration() {
        return mFadeDuration;
    }

    public void setFadeDuration(long fadeDuration) {
        this.mFadeDuration = fadeDuration;
    }

    public Shape getShape() {
        return mShape;
    }

    public void setShape(Shape shape) {
        this.mShape = shape;
    }

    public void setShapePadding(int padding) {
        this.mShapePadding = padding;
    }

    public int getShapePadding() {
        return mShapePadding;
    }

    public boolean getRenderOverNavigationBar() {
        return renderOverNav;
    }

    public void setRenderOverNavigationBar(boolean renderOverNav) {
        this.renderOverNav = renderOverNav;
    }
}
