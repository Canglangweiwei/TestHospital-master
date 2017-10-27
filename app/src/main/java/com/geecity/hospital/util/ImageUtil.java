package com.geecity.hospital.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片工具类
 *
 * @author LiuPeng
 */
@SuppressWarnings("ALL")
public class ImageUtil {

    private static final String TAG = "ImageUtil";

    /**
     * 从URL获取Bitmap
     *
     * @param urlPath 远程图片地址
     * @return Bitmap 获取到的bitmap
     * @throws IOException
     */
    public static Bitmap getBitmapFromUrl(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }

    /**
     * 从网络或者本地获取bitmap,并缓存到本地
     *
     * @param url  图片网络地址
     * @param path 图片本地地址
     * @return 获取到的bitmap
     * @throws IOException
     */
    public static Bitmap getBitmapFromUrl(String url, String path) throws IOException {
        if (path == null || "".equals(path)) {
            return getBitmapFromUrl(url);
        }
        //先尝试从本地获取，本地获取失败则从网络获取,并保存到本地
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            bitmap = getBitmapFromUrl(url);
            if (bitmap != null) {
                saveBitmapToFile(bitmap, path);
            }
        }
        return bitmap;
    }

    /**
     * 从文件获取图片
     *
     * @param file 文件名
     * @return bitmap
     */
    public static Bitmap getBitmapFromFile(String file) {
        return BitmapFactory.decodeFile(file);
    }

    /**
     * 保存图片到文件系统
     *
     * @param fileName 文件名
     * @param bitmap   图片
     */
    public static void saveBitmapToFile(Bitmap bitmap, String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            File path = file.getParentFile();
            path.mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按质量压缩图片
     *
     * @param srcBitmap 原图片
     * @param quality   质量百分比
     * @return 新图片，原图片会被回收；压缩失败返回原图片
     */
    public static Bitmap compressImage(Bitmap srcBitmap, int quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        byte[] imgByte = out.toByteArray();
        try {
            out.flush();
            out.close();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            srcBitmap.recycle();
            srcBitmap = null;
            System.gc();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return srcBitmap;
    }

    /**
     * 从资源中获取图片
     *
     * @param res    资源
     * @param resId  资源id
     * @param width  宽度
     * @param height 高度
     * @return bitmap
     */
    public static Bitmap getBitmapFromResource(Resources res, int resId, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        Log.i(TAG, "原始图片宽度为：" + options.outWidth + "，高度为：" + options.outHeight);
        /*使用inSamleSize进行第一步压缩（按比例压缩）
          inSmaleSize参数是整形值，代表缩放1/N^2
          所以如果希望完全按照目标大小，需要进一步缩放。
         */
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        Log.i(TAG, "第一步压缩图片宽度为：" + bitmap.getWidth() +
                "，高度为：" + bitmap.getHeight() +
                "。缩放比例为1/" + options.inSampleSize);
        //进行进一步压缩(按照绝对分辨率压缩)
        //这里是按照给定的宽高绝对值来进行缩放，所以需要计算宽高的比例
        //如果压缩到目标分辨率之后还是不能满足，则可以用bitmap.compress方法进行质量的压缩
        bitmap = getScaleBitmap(bitmap, width, width * options.outHeight / options.outWidth);
        Log.i(TAG, "最终压缩图片宽度为：" + bitmap.getWidth() + "，高度为：" + bitmap.getHeight());
        return bitmap;
    }

    /**
     * 获取缩放比例
     *
     * @param options   图片参数
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * @param srcBitmap 源bitmap
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @description 通过传入的bitmap、目标分辨率，进行压缩，得到符合标准的bitmap
     */
    public static Bitmap getScaleBitmap(Bitmap srcBitmap, int reqWidth, int reqHeight) {
        // filter决定放大图片是否平滑，这里是缩小图片，设置为false
        Bitmap newBitmap = Bitmap.createScaledBitmap(srcBitmap, reqWidth, reqHeight, false);
        // 如果指定的宽度和高度与原图的相等，这个方法会直接返回原bitmap，所以要判断一下是否相等再将原bitmap释放
        if (srcBitmap != newBitmap) {
            srcBitmap.recycle();
            System.gc();
        }
        return newBitmap;
    }
}
