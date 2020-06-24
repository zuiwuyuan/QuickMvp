package com.njfu.yxcmc.module.image;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageUtil;
import com.donkingliang.imageselector.utils.UriUtils;
import com.donkingliang.imageselector.utils.VersionUtils;
import com.njfu.yxcmc.R;
import com.njfu.yxcmc.base.BaseActivity;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * 从图库选择图片或直接拍照
 * https://github.com/donkingliang/ImageSelector
 *
 * @author Lolicon
 */
public class WeChatSelectImgsActivity extends BaseActivity {

    private static final int REQUEST_CODE = 0x1001;

    private ImageView ivPhoto;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wechat_select_imgs;
    }

    @Override
    public void initView() {
        ivPhoto = findViewById(R.id.ivPhoto);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tvBtn1, R.id.tvBtn2, R.id.tvBtn3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBtn1:
                //仅拍照
                ImageSelector.builder()
                        .onlyTakePhoto(true)  // 仅拍照，不打开相册
                        .start(this, REQUEST_CODE);
                break;
            case R.id.tvBtn2:
                ImageSelector.preload(this);
                //单选
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(true)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .start(this, REQUEST_CODE); // 打开相册

//          //限数量的多选(比如最多9张)
//        ImageSelector.builder()
//                .useCamera(true) // 设置是否使用拍照
//                .setSingle(false)  //设置是否单选
//                .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
//                .setSelected(selected) // 把已选的图片传入默认选中。
//                .canPreview(true) //是否可以预览图片，默认为true
//                .start(this, REQUEST_CODE); // 打开相册

////不限数量的多选
//        ImageSelector.builder()
//                .useCamera(true) // 设置是否使用拍照
//                .setSingle(false)  //设置是否单选
//                .setMaxSelectCount(0) // 图片的最大选择数量，小于等于0时，不限数量。
//                .setSelected(selected) // 把已选的图片传入默认选中。
//                .canPreview(true) //是否可以预览图片，默认为true
//                .start(this, REQUEST_CODE); // 打开相册
                break;
            case R.id.tvBtn3:
                ImageSelector.preload(this);
                //单选并剪裁
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setCrop(true)  // 设置是否使用图片剪切功能。
                        .setCropRatio(1.0f) // 图片剪切的宽高比,默认1.0f。宽固定为手机屏幕的宽。
                        .setSingle(true)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .start(this, REQUEST_CODE); // 打开相册

////拍照并剪裁
//        ImageSelector.builder()
//                .setCrop(true) // 设置是否使用图片剪切功能。
//                .setCropRatio(1.0f) // 图片剪切的宽高比,默认1.0f。宽固定为手机屏幕的宽。
//                .onlyTakePhoto(true)  // 仅拍照，不打开相册
//                .start(this, REQUEST_CODE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);

            if (images != null && images.size() > 0) {
                boolean isAndroidQ = VersionUtils.isAndroidQ();
                if (isAndroidQ) {
                    // 是否是剪切返回的图片
                    boolean isCropImg = ImageUtil.isCutImage(this, images.get(0));
                    if (isCropImg) {
                        Glide.with(this).load(images.get(0)).into(ivPhoto);
                    } else {
                        //图片链接转uri
                        Uri uri = UriUtils.getImageContentUri(this, images.get(0));
                        //通过uri加载图片
                        Glide.with(this).load(uri).into(ivPhoto);
                    }
                } else {
                    Glide.with(this).load(images.get(0)).into(ivPhoto);
                }
            }

            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);

            LogUtils.e(isCameraImage);
            LogUtils.e(images);
        }
    }

}