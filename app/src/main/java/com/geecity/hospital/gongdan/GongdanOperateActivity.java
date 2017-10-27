package com.geecity.hospital.gongdan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geecity.hospital.R;
import com.geecity.hospital.adapter.NinePicturesAdapter;
import com.geecity.hospital.base.AbsBaseActivity;
import com.geecity.hospital.bean.GongdanDetailBean;
import com.geecity.hospital.gongdan.api.ApiServiceProtocol;
import com.geecity.hospital.gongdan.api.OperateApiService;
import com.geecity.hospital.util.rxbus.RxBus;
import com.geecity.hospital.util.rxbus.RxEventIndicator;
import com.geecity.hospital.xgohttp.WebAPIListener;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NoScrollGridView;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>
 * 工单完成页面
 * </p>
 * Created by Administrator on 2017/10/12 0012.
 */
public class GongdanOperateActivity extends AbsBaseActivity {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.add_gdfinish_edit_miaoshu)
    EditText etContent;
    @Bind(R.id.count)
    TextView mTextView;
    @Bind(R.id.gridview)
    NoScrollGridView gridview;

    @Bind(R.id.btn_gongdan_wc)
    Button btn_wc;

    private String gdid, gdtype;// 工单id

    private ApiServiceProtocol apiServiceProtocol;

    // 图片数量
    private static final int IMAGE_NUMBER = 9;
    private NinePicturesAdapter ninePicturesAdapter;
    private final int REQUEST_CODE = 120;
    private final int REQUEST_TAKE_PHOTO_PERMISSION = 222;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_gongdan_operate;
    }

    @Override
    protected void parseIntent() {
        // 获取页面传值
        if (getIntent().getExtras() != null) {
            gdid = getIntent().getExtras().getString("gdid");
            gdtype = getIntent().getExtras().getString("type");
        }
    }

    @Override
    protected void initUi() {
        ntb.setTitleText(gdtype);
        apiServiceProtocol = new ApiServiceProtocol();
    }

    @Override
    protected void initDatas() {
        ninePicturesAdapter = new NinePicturesAdapter(this, IMAGE_NUMBER, new NinePicturesAdapter.OnClickAddListener() {
            @Override
            public void onClickAdd(int position) {
                requestCamera();
            }
        });
        gridview.setAdapter(ninePicturesAdapter);
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        etContent.setSingleLine(false);
        etContent.addTextChangedListener(mTextWatcher);
        etContent.setSelection(etContent.length());
        // 完成
        RxView.clicks(btn_wc)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {

                    @Override
                    public void call(Void aVoid) {
                        // 完成
                        detailLastNewState();
                    }
                });
    }

    /**
     * 申请打开相机权限
     */
    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(GongdanOperateActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GongdanOperateActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO_PERMISSION);
        } else if (ContextCompat.checkSelfPermission(GongdanOperateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GongdanOperateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_PHOTO_PERMISSION);
        } else if (ContextCompat.checkSelfPermission(GongdanOperateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GongdanOperateActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_TAKE_PHOTO_PERMISSION);
        } else {
            choosePhoto();
        }
    }

    /**
     * EditText的监听事件
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        private int editEnd;

        @Override
        public void afterTextChanged(Editable s) {

            editStart = etContent.getSelectionStart();
            editEnd = etContent.getSelectionEnd();

			/* 先去掉监听器，否则会出现栈溢出 */
            etContent.removeTextChangedListener(mTextWatcher);

            /**
             * 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
             * 因为是中英文混合，单个字符而言，calculateLength函数都会返回1 当输入字符个数超过限制的大小时，
             * 进行截断操作
             */
            while (calculateLength(s.toString()) > MAX_COUNT) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            /* 将这行代码注释掉就不会出现后面所说的输入法在数字界面自动跳转回主界面的问题了 */
            // mEditText.setText(s);
            etContent.setSelection(editStart);
            /* 恢复监听器 */
            etContent.addTextChangedListener(mTextWatcher);
            setLeftCount();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    /**
     * 计算字符个数
     *
     * @param c 字符
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    private static final int MAX_COUNT = 100;

    /**
     * 标记剩余字符
     */
    private void setLeftCount() {
        mTextView.setText("还可以输入"
                + String.valueOf((MAX_COUNT - getInputCount())) + "个字符！");
    }

    /**
     * 计算输入的字符个数
     */
    private long getInputCount() {
        return calculateLength(etContent.getText().toString().trim());
    }

    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(true)
                // 确定按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                .titleBgColor(ContextCompat.getColor(this, R.color.main_color))
                // 使用沉浸式状态栏
                .statusBarColor(ContextCompat.getColor(this, R.color.main_color))
                // 返回图标ResId
                .backResId(R.drawable.icon_back)
                .title("图片")
                // 第一个是否显示相机
                .needCamera(true)
                // 需要裁剪
                .needCrop(true)
                // 最大选择图片数量
                .maxNum(9 - ninePicturesAdapter.getPhotoCount())
                .build();
        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            ImageLoaderUtils.display(context, imageView, path);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 申请成功，可以拍照
                choosePhoto();
            } else {
                ToastUitl.showShort("相机权限已被禁止, 请在设置中允许打开");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE:// 相册选择
                    List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                    if (ninePicturesAdapter.getPhotoCount() + pathList.size() > IMAGE_NUMBER) {
                        ToastUitl.showShort("最多上传" + IMAGE_NUMBER + "张图片");
                        return;
                    }
                    if (ninePicturesAdapter != null) {
                        ninePicturesAdapter.addAll(pathList);
                    }
                    break;
            }
        }
    }

    /**
     * 获取工单最新操作状态
     */
    private void detailLastNewState() {
        startProgressDialog();
        TreeMap<String, Object> params = new TreeMap<>();
        params.put("gdid", gdid);
        apiServiceProtocol.detailLastNewState(params)
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String dataJson) {
                        stopProgressDialog();
                        // 解析首页信息
                        if (TextUtils.isEmpty(dataJson)) {
                            ToastUitl.showShort("首页信息加载失败");
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(dataJson);
                            if (!json.getBoolean("success")) {
                                ToastUitl.showShort(json.getString("message"));
                                return;
                            }
                            String result = json.getString("data");
                            Type type = new TypeToken<GongdanDetailBean>() {
                            }.getType();
                            GongdanDetailBean detailBean = mGson.fromJson(result, type);
                            if (detailBean == null) {
                                ToastUitl.showShort("数据同步异常，请稍后处理");
                                return;
                            }
                            // 当前登录人
                            String username = mBaseUserBean.getUserName();
                            if (!username.equals(detailBean.getCzr())) {
                                ToastUitl.showShort("当前登录人权限不足");
                                return;
                            }
                            if (!"处理中".equals(detailBean.getZt())) {
                                ToastUitl.showShort("当前节点不允许执行相关操作");
                                return;
                            }
                            doProgress(mBaseUserBean.getUserName());
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        stopProgressDialog();
                    }
                });
    }

    private MDAlertDialog mdAlertDialog;

    /**
     * 确认、完成
     *
     * @param slr 受理人
     */
    private void doProgress(final String slr) {
        String desc = "";
        if ("确认".equals(gdtype)) {
            desc = "您要执行确认操作吗？";
        } else if ("完成".equals(gdtype)) {
            desc = "【" + slr + "】,您的任务确定已经完成？";
        }

        mdAlertDialog = new MDAlertDialog.Builder(this)
                .setHeight(0.25f)  //屏幕高度*0.3
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText(desc)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("确认")
                .setRightButtonTextColor(R.color.gray)
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setButtonTextSize(14)
                .setOnclickListener(new DialogOnClickListener() {

                    @Override
                    public void clickLeftButton(View view) {
                        mdAlertDialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        mdAlertDialog.dismiss();
                        submit(slr);
                    }
                })
                .build();
        if (!isFinishing())
            mdAlertDialog.show();
    }

    /**
     * 执行汇报进展操作
     *
     * @param slr 受理人
     */
    private void submit(String slr) {
        startProgressDialog();
        final OperateApiService mApiService = new OperateApiService(this);
        mApiService.setGdid(gdid);
        mApiService.setGdzt(gdtype);
        mApiService.setSlr(slr);
        mApiService.setContent(etContent.getText().toString().trim());
        mApiService.setImages(ninePicturesAdapter.getData());
        mApiService.setUsername(mBaseUserBean.getUserName());
        mApiService.setListener(new WebAPIListener() {
            @Override
            public void onLoadSuccess(int successCode) {
                stopProgressDialog();
                ToastUitl.showShort(mApiService.getPromptMsg());
                onBackPressed();
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                stopProgressDialog();
                ToastUitl.showShort(errorMessage);
            }
        });
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().postEvent(new RxEventIndicator(3001, "updateDetailUI"));
        super.onDestroy();
    }
}
