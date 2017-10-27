package com.geecity.hospital.util;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author LiuPeng
 */
@SuppressWarnings("ALL")
public class FileUtil {

    public static final String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String ADPATH = FileUtil.SDPATH + "/Geecity_MVP";

    public static void createSdDir() {
        File file = new File(FileUtil.ADPATH);
        if (!file.exists()) {
            boolean create = file.mkdirs();
            Log.d("FileUtil", "create = " + create);
        } else {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdir();
            }
        }
    }

    public static boolean isFileExist(String paramString) {
        if (paramString == null)
            return false;
        File localFile = new File(ADPATH + "/" + paramString);
        if (localFile.exists()) {
            return true;
        }
        return false;
    }

    public static File createFile(String fileName) throws IOException {
        File file = new File(ADPATH, fileName);
        file.createNewFile();
        return file;
    }

    public static List<String> getAllAD() {
        File file = new File(FileUtil.ADPATH);
        File[] fileList = file.listFiles();
        List<String> list = new ArrayList<>();
        if (null != fileList) {
            for (File f : fileList) {
                list.add(f.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * 将文件转换为base64字符串
     *
     * @param filePath 文件路径
     * @return 字符串
     */
    public static String file2Base64(String filePath) {
        File file = new File(filePath);
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
