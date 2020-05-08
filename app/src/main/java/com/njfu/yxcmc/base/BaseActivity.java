package com.njfu.yxcmc.base;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.njfu.yxcmc.R;
import com.njfu.yxcmc.util.ToastUtil;
import com.njfu.yxcmc.widget.dialog.CustomProgressDialog;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity {

    public Unbinder unBinder;

    /**
     * 正在加载弹出框
     */
    private Dialog mLoadingDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏

        setContentView(this.getLayoutId());

        unBinder = ButterKnife.bind(this);
        initImmersionBar();
        initView();
        initData();

        initListener();
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar
                .with(this)
                .transparentStatusBar()
                .init();
    }

    public void toastMsg(String msg) {
        ToastUtil.INSTANCE.showToast(this,msg);
    }

    public void toastMsg(int msgId) {
        ToastUtil.INSTANCE.showToast(this, getResources().getString(msgId));
    }

    /**
     * 显示进度加载框
     *
     * @param msgId 文字id
     */
    protected void showLoadingDialog(String msg, int msgId) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        // 显示进度框
        mLoadingDialog = new CustomProgressDialog(BaseActivity.this, R.style.prompt_style, msg, msgId);

        try {
            mLoadingDialog.show();
        } catch (WindowManager.BadTokenException ex) {
            // dialog依附的activity如果被销毁，会抛出改异常
        }
    }

    /**
     * 隐藏加载框
     */
    protected void dismissLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /**
     * 关闭当前页面
     */
    public void back() {
        this.finish();
    }

    @Override
    protected void onDestroy() {

        try {
            if (unBinder != null) {
                unBinder.unbind();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    protected void initListener() {
    }
}
