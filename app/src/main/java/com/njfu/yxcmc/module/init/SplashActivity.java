package com.njfu.yxcmc.module.init;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.njfu.yxcmc.R;
import com.njfu.yxcmc.base.BaseMvpActivity;
import com.njfu.yxcmc.base.GloabApp;
import com.njfu.yxcmc.contract.SplashContract;
import com.njfu.yxcmc.module.login.LoginActivity;
import com.njfu.yxcmc.presenter.SplashPresenter;
import com.njfu.yxcmc.util.ScreenUtils;
import com.njfu.yxcmc.widget.dialog.AgreementDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseMvpActivity<SplashPresenter> implements SplashContract.View {

    private static final String[] permissionsGroup = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int ACTIVITY_REQUEST_CODE_TO_SYSTEM_SETTING = 0x1C03;// 调用系统设置时的Activity请求码

    @BindView(R.id.imgSplashBg)
    public ImageView imgSplashBg;
    @BindView(R.id.imgLoading)
    public ImageView imgLoading;

    private boolean isBackFromOtherInnerPage = false;

    private boolean isFromSystemSetting = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

        mPresenter = new SplashPresenter(this);
        mPresenter.attachView(this);

    }

    @Override
    public void initData() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightPixels = ScreenUtils.getScreenHeight(this);
        int widthPixels = ScreenUtils.getScreenWidth(this);
        float density = dm.density;
        float heightDP = heightPixels / density;
        float widthDP = widthPixels / density;

        float smallestWidthDP;
        if (widthDP < heightDP) {
            smallestWidthDP = widthDP;
        } else {
            smallestWidthDP = heightDP;
        }

//        LogUtils.e("smallestWidthDP : " + smallestWidthDP);

        if (GloabApp.getInstance().getPrefs().getBoolean("IsFirstLaunchApp", true)) {
            showPrivacyStatement();
        } else {
            checkAccessFilePermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isFromSystemSetting) {
            if (isBackFromOtherInnerPage) {
                isBackFromOtherInnerPage = false;
                checkAccessFilePermission();
            }
        }

        isFromSystemSetting = false;
    }

    private void showPrivacyStatement() {
        // 第一次进入应用，跳转到引导页
        String promotStr = "欢迎使用 "
                + getString(R.string.app_name)
                + "\n应用使用过程中会涉及以下特殊权限：网络、读写文件，以及其他基础权限。\n相关权限的调用，仅用于实现相关的功能或提高应用的使用性。这些权限为系统开放权限，绝不涉及用户的个人隐私信息。";


        final AgreementDialog.MyBuilder builder = new AgreementDialog.MyBuilder("提  示", promotStr, "不同意", "同 意");
        AgreementDialog dialog = new AgreementDialog(SplashActivity.this, builder, new AgreementDialog.Callback() {
            @Override
            public void onCancelBtnClick(AgreementDialog dialog) {
                SplashActivity.this.finish();
            }

            @Override
            public void onConfirmBtnClick(AgreementDialog dialog) {
                dialog.dismiss();
                checkAccessFilePermission();
                GloabApp.getInstance().getPrefs().saveBoolean("IsFirstLaunchApp", false);
            }

            @Override
            public void onTvHyperlinkClick(AgreementDialog dialog) {
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    @SuppressLint("CheckResult")
    private void checkAccessFilePermission() {

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEachCombined(permissionsGroup)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            startVersionCheck();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            onPermissionForbidden();
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』那么下次启动时，就不会提示出来了
                            onPermissionForbidden();
                        }
                    }
                });
    }

    private void onPermissionForbidden() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("权限提醒");
        builder.setMessage("\t\t为了应用的正常运行，请按“确认”按钮后，到设置中心的“权限管理”中开启本应用的文件存储权限。\n\n\t\t如您经常看到这个提示，请点击“取消”按钮，之后进入系统安全中心的授权管理，手动开启对应权限。");
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package", getPackageName(), null);

                intent.setData(uri);
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE_TO_SYSTEM_SETTING);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();

            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTIVITY_REQUEST_CODE_TO_SYSTEM_SETTING == requestCode) {

            checkAccessFilePermission();

            isFromSystemSetting = true;
        }
    }

    /**
     * 启动版本检测
     */
    private void startVersionCheck() {

        mPresenter.checkUpdate();
    }

    @Override
    public void showLoading() {
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        imgLoading.post(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils
                        .loadAnimation(imgLoading.getContext(), R.anim.rotate_refresh_drawable_default);
                // 开始动画
                imgLoading.startAnimation(rotateAnimation);

            }
        });
    }

    @Override
    public void hideLoading() {
        imgLoading.clearAnimation();
    }

    @Override
    public void onSuccess(JSONObject upgradeObj) {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        this.finish();
    }
}
