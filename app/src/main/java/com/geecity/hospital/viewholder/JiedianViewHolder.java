package com.geecity.hospital.viewholder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.geecity.hospital.R;
import com.geecity.hospital.bean.JiedianBean;
import com.geecity.hospital.widget.MultiImageView;
import com.jaydenxiao.common.imagePager.BigImagePagerActivity;

import java.util.List;

/**
 * des：节点viewholder
 * Created by weiwei on 2017/06/14
 */
@SuppressWarnings("ALL")
public class JiedianViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private View itemView;

    /**
     * 圆点
     */
    private ImageView dotIv1;

    /**
     * 圆点
     */
    private ImageView dotIv2;

    /**
     * 节点状态
     */
    private TextView ztTv;

    /**
     * 操作时间
     */
    private TextView timeTv;

    /**
     * 操作时间
     */
    private TextView jiedianTv;

    /**
     * 操作人
     */
    private TextView czrTv;

    /**
     * 操作说明
     */
    private TextView czsmTv;

    /**
     * 图片
     */
    private MultiImageView multiImageView;

    public static JiedianViewHolder create(Context context) {
        JiedianViewHolder imageViewHolder = new JiedianViewHolder(LayoutInflater.from(context).inflate(R.layout.item_timeline, null), context);
        return imageViewHolder;
    }

    public JiedianViewHolder(View itemView, final Context context) {
        super(itemView);
        this.itemView = itemView;
        this.mContext = context;
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        ViewStub linkOrImgViewStub = (ViewStub) itemView.findViewById(R.id.linkOrImgViewStub);
        linkOrImgViewStub.setLayoutResource(R.layout.item_circle_viewstub_imgbody);
        linkOrImgViewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) itemView.findViewById(R.id.multiImageView);
        if (multiImageView != null) {
            this.multiImageView = multiImageView;
        }
        dotIv1 = (ImageView) itemView.findViewById(R.id.iv_dot1);
        dotIv2 = (ImageView) itemView.findViewById(R.id.iv_dot2);
        ztTv = (TextView) itemView.findViewById(R.id.txt_zt);
        timeTv = (TextView) itemView.findViewById(R.id.txt_time);
        jiedianTv = (TextView) itemView.findViewById(R.id.txt_jiedian);
        czrTv = (TextView) itemView.findViewById(R.id.txt_czr);
        czsmTv = (TextView) itemView.findViewById(R.id.txt_czsm);
    }

    /**
     * 设置数据
     */
    public void setData(final JiedianBean jiedianBean, int type) {
        if (jiedianBean == null) {
            return;
        }

        ztTv.setText(jiedianBean.getZt());              // 节点状态
        timeTv.setText(jiedianBean.getCztime());        // 操作时间
        jiedianTv.setText(jiedianBean.getTitle());     //  节点
        czrTv.setText(jiedianBean.getCzr());            // 操作人
        czsmTv.setText(jiedianBean.getCzcontent());     // 操作说明

        // 最新节点显示方式
        if (type == 1) {
            dotIv1.setImageResource(R.drawable.bg_green_circle_smic);
            dotIv1.setVisibility(View.VISIBLE);
            dotIv2.setVisibility(View.GONE);
        } else {
            dotIv2.setImageResource(R.drawable.shape_normal_item_main);
            dotIv1.setVisibility(View.GONE);
            dotIv2.setVisibility(View.VISIBLE);
        }

        final List<String> photos = jiedianBean.getPhotos();

        // 图片
        if (photos == null || photos.size() == 0) {
            multiImageView.setVisibility(View.GONE);
        } else {
            multiImageView.setVisibility(View.VISIBLE);
            multiImageView.setList(photos);
            multiImageView.setOnItemClickListener(new MultiImageView.OnEveryPhotoClickListener() {

                @Override
                public void onPhotoClick(View view, int position) {
                    /**
                     * 查看大图
                     */
                    BigImagePagerActivity.startImagePagerActivity((AppCompatActivity) mContext, photos, position);
//                    Intent intent = GongdanPhotoDetailActivity.deliverPhotoDetailIntent(mContext, position, jiedianBean);
//                    ((AbsBaseActivity) mContext).startActivity(intent);
                }
            });
        }
    }
}
