package com.geecity.hospital.xgohttp;

import android.net.Uri;

import com.geecity.hospital.xgohttp.interceptor.XgoLogInterceptor;
import com.jaydenxiao.common.commonutils.XgoLog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;


/**
 * XgoHttpClient
 * for okHttp simple encapsulation
 * Created by weiwei on 2015/12/11.
 */
@SuppressWarnings("ALL")
public class XgoHttpClient {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    private static final XgoHttpClient mClient = new XgoHttpClient();
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        // 添加日志过滤器
        XgoLogInterceptor logInterceptor = new XgoLogInterceptor(new XgoLogInterceptor.Logger() {

            @Override
            public void log(String message) {
                XgoLog.logd("获取结果集：" + message);
            }
        });
        logInterceptor.setLevel(XgoLogInterceptor.Level.BODY);
        mOkHttpClient.interceptors().add(logInterceptor);
    }

    public static XgoHttpClient getClient() {
        return mClient;
    }

    /**
     * 同步模式
     *
     * @return String(json)
     */
    public String execute2String(Request request) {
        String result = null;
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 异步CallBack模式
     */
    public void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 通过http请求的基本信息，创建一个Request对象
     */
    public Request getRequest(String url, String method, Map<String, Object> params) {
        if (params == null) {
            params = new TreeMap<>();
        }

        Request.Builder builder = new Request.Builder();

        if (XgoHttpClient.METHOD_GET.equalsIgnoreCase(method)) {
            // GET
            builder.url(initGetRequest(url, params)).get();
        } else if (XgoHttpClient.METHOD_POST.equalsIgnoreCase(method)) {
            // POST
            builder.url(url).post(initRequestBody(params));
        } else if (XgoHttpClient.METHOD_PUT.equalsIgnoreCase(method)) {
            // PUT
            builder.url(url).put(initRequestBody(params));
        } else if (XgoHttpClient.METHOD_DELETE.equalsIgnoreCase(method)) {
            // DELETE
            if (params.size() == 0) {
                builder.url(url).delete();
            } else {
                builder.url(url).delete(initRequestBody(params));
            }
        }
        return builder.build();
    }

    /**
     * 初始化Body类型请求参数
     * init Body type params
     */
    private RequestBody initRequestBody(Map<String, Object> params) {
        MultipartBuilder bodyBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        XgoLog.logw("打印参数：---------- start ---------");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof File) {
                File file = (File) value;
                try {
                    FileNameMap fileNameMap = URLConnection.getFileNameMap();
                    String mimeType = fileNameMap.getContentTypeFor(file.getAbsolutePath());
                    XgoLog.logw("mimeType::" + mimeType);
                    bodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(mimeType), file));
                } catch (Exception e) {
                    e.printStackTrace();
                    XgoLog.logw("mimeType is Error !");
                }
            } else {
                builder.append(key).append("=").append(value).append("&");
                XgoLog.logw(key + "::" + value);
                bodyBuilder.addFormDataPart(key, value.toString());
            }
        }
        XgoLog.logw(builder.toString());
        XgoLog.logw("打印参数：---------- end ---------");
        return bodyBuilder.build();
    }

    /**
     * 初始化Get请求参数
     * init Get type params
     */
    private String initGetRequest(String url, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(url);
        // has params ?
        if (params.size() > 0) {
            sb.append('?');
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            int count = 0;
            for (Map.Entry entry : entries) {
                count++;
                sb.append(entry.getKey()).append('=').append(Uri.encode(entry.getValue().toString()));
                if (count == params.size()) {
                    break;
                }
                sb.append('&');
            }
            url = new String(sb);
        }
        XgoLog.logw("打印GET请求地址：" + url);
        return url;
    }

    /**
     * set timeout
     */
    public void setConnectTimeout(long time) {
        mOkHttpClient.setConnectTimeout(time, TimeUnit.MILLISECONDS);
    }
}
