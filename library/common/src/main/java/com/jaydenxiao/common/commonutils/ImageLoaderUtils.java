package com.jaydenxiao.common.commonutils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaydenxiao.common.R;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    /**
     * @param context     上下文环境
     * @param imageView   图片加载器
     * @param url         图片地址
     * @param placeholder 图片初始化
     * @param error       图片加载错误时显示
     */
    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .placeholder(placeholder)
                .error(error)
                .crossFade()
                .into(imageView);
    }

    /**
     * 图片展示
     *
     * @param context   上下文环境
     * @param imageView 图片加载器
     * @param url       图片地址
     */
    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .crossFade()
                .into(imageView);
    }

    /**
     * 图片展示
     *
     * @param context   上下文环境
     * @param imageView 图片加载器
     * @param url       图片文件
     */
    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载小图
     *
     * @param context   上下文环境
     * @param imageView 图片加载器
     * @param url       图片地址
     */
    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .thumbnail(0.5f)
                .into(imageView);
    }

    /**
     * 加载大图
     *
     * @param context   上下文环境
     * @param imageView 图片加载器
     * @param url       图片地址
     */
    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param context   上下文环境
     * @param imageView 图片加载器
     * @param url       图片地址
     */
    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .crossFade()
                .into(imageView);
    }

    /**
     * 展示圆形图片
     *
     * @param context   上下文环境
     * @param imageView 图片加载器
     * @param url       图片地址
     */
    public static void displayRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_content_tip)
                .centerCrop()
                .transform(new GlideRoundTransformUtil(context))
                .into(imageView);
    }
}
