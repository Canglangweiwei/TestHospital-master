package com.geecity.hospital.gongdan.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.geecity.hospital.util.FileUtil;
import com.geecity.hospital.util.ImageUtil;
import com.geecity.hospital.xgohttp.IHttpPostApi;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 提交拍照信息
 * </p>
 * Created by Administrator on 2017/9/8 0008.
 */
public class OperateApiService extends IHttpPostApi {

    private String gdid;                     // 客户服务id
    private String gdzt;                          // 工单状态
    private String username;                          // 工单状态
    private String content;                     // 描述信息
    private String slr;                         // 操作人
    private List<String> images;                // 图片信息

    private String promptMsg;                   // 提示信息

    public String getPromptMsg() {
        return promptMsg;
    }

    private void setPromptMsg(String promptMsg) {
        this.promptMsg = promptMsg;
    }

    /**
     * 构造器
     *
     * @param context 上下文环境
     */
    public OperateApiService(Context context) {
        super(context);
    }

    @Override
    protected void getInputParam(HashMap<String, Object> params) {
        params.put("gdid", gdid);
        params.put("gdzt", gdzt);
        params.put("username", username);
        params.put("slr", slr);
        params.put("content", content);
        // 将图片转换成base64
        if (images != null && images.size() > 0) {
            StringBuilder allUrl = new StringBuilder();
            for (int i = 0; i < images.size(); i++) {
                String base64;
                if (TextUtils.isEmpty(images.get(i)))
                    continue;
                // 图片大于720p则按照720比例压缩图片
                Bitmap bitmap = ImageUtil.getBitmapFromFile(images.get(i));
                if (bitmap == null)
                    continue;
                if (bitmap.getHeight() > 720 || bitmap.getWidth() > 720) {
                    int height = 720;
                    int width = (int) (height * (bitmap.getWidth() / (double) bitmap.getHeight()));
                    Bitmap scaleBitmap = ImageUtil.getScaleBitmap(bitmap, width, height);
                    ImageUtil.saveBitmapToFile(scaleBitmap, images.get(i));
                }
                // 转base64
                base64 = FileUtil.file2Base64(images.get(i));
                allUrl.append(base64).append(",");
            }
            String url = allUrl.toString();
            if (!TextUtils.isEmpty(allUrl)) {
                url = url.substring(0, url.lastIndexOf(","));
            }
            params.put("images", url);
        } else {
            params.put("images", "");
        }
    }

    @Override
    protected boolean analysisOutput(String result) throws JSONException {
        setPromptMsg(result);
        return true;
    }

    @Override
    protected String getMethodName() {
        return "gongdan/operate.php";
    }

    public void setGdid(String gdid) {
        this.gdid = gdid;
    }

    public void setGdzt(String gdzt) {
        this.gdzt = gdzt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSlr(String slr) {
        this.slr = slr;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
