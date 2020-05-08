package com.njfu.yxcmc.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.njfu.yxcmc.R;

/**
 * 自定义进度框
 */
public class CustomProgressDialog extends Dialog {

    private ImageView mImageView;

    private TextView mLoadingTv;

    private String mLoadingString;
    private int mLoadingId;

    public CustomProgressDialog(Context context, int themeResId, String loadingString, int loadingId) {
        super(context, themeResId);
        this.mLoadingString = loadingString;
        this.mLoadingId = loadingId;

        setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {

        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils
                        .loadAnimation(mImageView.getContext(), R.anim.rotate_refresh_drawable_default);
                // 开始动画
                mImageView.startAnimation(rotateAnimation);

            }
        });

        if (!TextUtils.isEmpty(mLoadingString)) {
            mLoadingTv.setText(mLoadingString);
        } else if (mLoadingId != 0) {
            mLoadingTv.setText(mLoadingId);
        } else {
            mLoadingTv.setText("");
        }
    }

    public void setContent(String str) {
        mLoadingTv.setText(str);
    }

    private void initView() {
        setContentView(R.layout.dialog_frame_load);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }
}
