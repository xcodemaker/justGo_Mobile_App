package com.dhammika_dev.justgo.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.RequestOptions;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.ui.utils.GlideTransformation;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class WahImageView extends CircleImageView {

    private Transformation glideTransformation;
    private int placeholderResId;
    private int errorResId;
    private int mHeight, mWidth;

    public WahImageView(Context context) {
        super(context);
    }

    public WahImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WahImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleCustomAttrs(context, attrs, defStyleAttr, 0);
    }


    @TargetApi(21)
    public WahImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        // super();
        handleCustomAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    private void handleCustomAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WahImageView, defStyleAttr, defStyleRes);
        BitmapPool pool = Glide.get(getContext()).getBitmapPool();
        int cornerRadius = a.getDimensionPixelSize(R.styleable.WahImageView_img_corner_radius, 0);


        if (a.getBoolean(R.styleable.WahImageView_is_circular, false) || cornerRadius > 0) {
            glideTransformation = new GlideTransformation(pool, a.getDimensionPixelSize(R.styleable.WahImageView_img_border_width, 0)
                    , a.getColor(R.styleable.WahImageView_img_border_color, Color.TRANSPARENT)
                    , cornerRadius);
        }

        errorResId = a.getResourceId(R.styleable.WahImageView_error_img, -1);
        placeholderResId = a.getResourceId(R.styleable.WahImageView_placeholder_img, -1);
    }

    public void setImageSize(int height, int width) {
        this.mHeight = height;
        this.mWidth = width;
    }

    public void setErrorImgResId(int resId) {
        errorResId = resId;
    }

    public void setPlaceholderResId(int resId) {
        placeholderResId = resId;
    }

    @Override
    public void setImageResource(int resId) {
        wahLoadImage(resId);
    }

    public void setImageUrl(String path) {
        wahLoadImage(path);
    }

    public void setImageUrl(Uri uri) {
        wahLoadImage(uri);
    }

    public void setImageUrl(File file) {
        wahLoadImage(file);
    }

    private void wahLoadImage(String path) {
        RequestBuilder<Drawable> request = Glide.with(getContext()).load(path);
        wahProcessRequest(request);
    }

    private void wahLoadImage(File file) {
        RequestBuilder<Drawable> request = Glide.with(getContext()).load(file);
        wahProcessRequest(request);
    }

    private void wahLoadImage(Uri uri) {
        RequestBuilder<Drawable> request = Glide.with(getContext()).load(uri);
        wahProcessRequest(Glide.with(getContext()).load(uri));
    }

    private void wahLoadImage(int resId) {
        //RequestBuilder<Drawable>
        RequestBuilder<Drawable> request = Glide.with(getContext()).load(resId);
        wahProcessRequest(request);
    }

    private void wahProcessRequest(RequestBuilder<Drawable> request) {
       /* if (mHeight > 0 && mWidth > 0) {
            request.override(mWidth, mHeight);
        } else if (getMeasuredHeight() > 0 && getMeasuredWidth() > 0) {
            request.override(getMeasuredWidth(), getMeasuredHeight());
        }*/
        RequestOptions requestOptions = new RequestOptions();

        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        if (placeholderResId != -1)
            requestOptions.placeholder(placeholderResId);
        if (errorResId != -1)
            requestOptions.error(errorResId);

        if (glideTransformation != null) {
            requestOptions.bitmapTransform(glideTransformation);
        }
        request.apply(requestOptions);
        request.into(this);
    }
}