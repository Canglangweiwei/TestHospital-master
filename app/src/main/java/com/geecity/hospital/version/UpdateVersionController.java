package com.geecity.hospital.version;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geecity.hospital.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@SuppressWarnings("ALL")
public class UpdateVersionController {

    private Context context;

    // 更新文件的实例
    private AppUpdateInfo info;
    // 当前版本号
    private int versionCode;
    // 提示用户更新的dialog
    private Dialog dialog;
    // 是否显示信息提示
    private boolean allowToast;

    public static UpdateVersionController getInstance(Context context, boolean allowToast) {
        return new UpdateVersionController(context, allowToast);
    }

    private UpdateVersionController(Context context, boolean allowToast) {
        this.context = context;
        this.allowToast = allowToast;
    }

    public void normalCheckUpdateInfo(AppUpdateInfo appUpdateInfo) {
        versionCode = getVerCode(context);// 等于19
        checkVersionTask(appUpdateInfo);
    }

    /**
     * 步骤一：获取版本信息
     */
    private void checkVersionTask(AppUpdateInfo appUpdateInfo) {
        // 网络加载获取app新版版本信息
        // 这里不做请求直接赋值
        this.info = appUpdateInfo;
        updateApp();
    }

    private void updateApp() {
        if (null != info && info.getVerCode() > versionCode) {// 20>19可更新
            showUpdataDialog();
        } else {
            if (allowToast) {
                Toast.makeText(context, "已经是最新版本啦~", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Button cancelBtn;

    /**
     * 步骤二：弹出对话框提示用户更新
     */
    private void showUpdataDialog() {

        dialog = new Dialog(context, android.R.style.Theme_Dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.activity_updater);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // 更新内容
        TextView contentText = (TextView) dialog.findViewById(R.id.content);
        contentText.setText(info.getVerInfo());

        cancelBtn = (Button) dialog.findViewById(R.id.cancel);
        cancelBtn.setVisibility(("1").equals(info.getUpdateFlag()) ? View.GONE : View.VISIBLE);
        // 取消更新
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 确认更新
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                downLoadApk();
                goToBrowserDownLoadApk(info.getUrl());
            }
        });
        dialog.findViewById(R.id.market).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(info.getUrl()));
                context.startActivity(intent);
            }
        });
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    /**
     * 步骤三：下载文件
     */
    private void downLoadApk() {
        // 进度条对话框
        //下载进度条
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("下载中...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        // 监听返回键--防止下载的时候点击返回
        pd.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    Toast.makeText(context, "正在下载请稍后", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });
        // Sdcard不可用
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD卡不可用~", Toast.LENGTH_SHORT).show();
        } else {
            if (!((Activity) context).isFinishing()) {
                pd.show();
            }
            //下载的子线程
            new Thread() {
                @Override
                public void run() {
                    try {
                        // 在子线程中下载APK文件
                        File file = getFileFromServer(info.getUrl(), pd);
                        sleep(1000);
                        // 安装APK文件
                        installApk(file);
                        pd.dismiss(); // 结束掉进度条对话框
                    } catch (Exception e) {
                        Log.d("apk", "文件下载失败了~");
                        pd.dismiss();
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * 去浏览器下载apk
     *
     * @param url 下载地址
     */
    private void goToBrowserDownLoadApk(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 从服务器下载apk
     */
    private File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength() / 1024);
            InputStream is = conn.getInputStream();

            File file = new File(Environment.getExternalStorageDirectory().getPath()
                    + "/Download/jucheng/", "geecity.apk");
            // 判断文件夹是否被创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total / 1024);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /**
     * 安装apk
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取版本名
     */
    private static String getVerName(Context context) {

        String verName = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            verName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取版本号
     */
    private static int getVerCode(Context context) {
        int verCode = -1;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);

            verCode = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verCode;
    }
}
