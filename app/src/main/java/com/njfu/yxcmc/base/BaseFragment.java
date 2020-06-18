package com.njfu.yxcmc.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.njfu.yxcmc.R;
import com.njfu.yxcmc.util.ToastUtil;
import com.njfu.yxcmc.widget.dialog.CustomProgressDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements View.OnTouchListener {

    protected Unbinder unBinder;
    protected View view = null;
    private Dialog mLoadingDialog = null;

    /**
     * 跳转到其他界面
     *
     * @param context    当前界面
     * @param bundle     传递参数
     * @param otherClass 目标界面
     * @param isFinish   跳转后是否关闭当前界面
     */
    public static void forwardActivity(Context context,
                                       Bundle bundle, Class<? extends BaseActivity> otherClass, boolean isFinish) {
        Intent intent = new Intent(context, otherClass);

        if (null != bundle) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if (isFinish) {
            ((Activity) context).finish();
        }
    }

    /**
     * 生成数据Bundle
     *
     * @param title       页面标题
     * @param aimFragment 目标Fragment
     * @param subBundle   传给Fragment页面的数据
     * @return Bundle实例
     */
    public static Bundle getBundleToAimPage(String title, Class<? extends BaseFragment> aimFragment, Bundle subBundle) {
        return getBundleToAimPage(title, null, aimFragment, subBundle);
    }

    /**
     * 生成数据Bundle
     *
     * @param title       页面标题
     * @param pageMenuId  页面MenuId
     * @param aimFragment 目标Fragment
     * @param subBundle   传给Fragment页面的数据
     * @return Bundle实例
     */
    protected static Bundle getBundleToAimPage(String title, String pageMenuId, Class<? extends BaseFragment> aimFragment, Bundle subBundle) {
        Bundle bundle = new Bundle();
        bundle.putString("TitleName", title);
        bundle.putSerializable("AimFragment", aimFragment);
        if (subBundle != null)
            bundle.putBundle("DataToAimFragment", subBundle);
        return bundle;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(this.getLayoutId(), container, false);
        } else {
            //  二次加载删除上一个子view
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
        }

        unBinder = ButterKnife.bind(this, view);
        initImmersionBar();
        initView();
        initData();
        initListener();

        return view;
    }

    protected void initImmersionBar() {
        ImmersionBar
                .with(this)
                .transparentStatusBar()
                .init();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onDestroyView() {

        try {
            if (unBinder != null) {
                unBinder.unbind();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroyView();

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected void initListener() {
    }

    protected void toastMsg(String msg) {
        ToastUtil.INSTANCE.showToast(getActivity(), msg);
    }

    protected void toastMsg(int msgId) {
        ToastUtil.INSTANCE.showToast(getActivity(), getResources().getString(msgId));
    }

    /**
     * 生成数据Bundle
     *
     * @param title       页面标题
     * @param aimFragment 目标Fragment
     * @return Bundle实例
     */
    protected Bundle getBundleToAimPage(String title, Class<? extends BaseFragment> aimFragment) {
        return getBundleToAimPage(title, aimFragment, null);
    }

    public boolean onBackPressed() {
        return true;
    }

    protected void finishActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    protected void finishActivityForResult(int stateCode, Bundle data) {
        if (getActivity() != null) {
            Intent intent = new Intent();
            if (data != null)
                intent.putExtras(data);
            getActivity().setResult(stateCode, intent);
            getActivity().finish();
        }
    }

    public void showLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        if (getActivity() == null) return;
        // 显示进度框
        if (null == mLoadingDialog) {
            mLoadingDialog = new CustomProgressDialog(getActivity(),
                    R.style.prompt_style, null, 0);
        }

        try {
            mLoadingDialog.show();
        } catch (Exception ex) {
            // dialog依附的activity如果被销毁，会抛出改异常
            LogUtils.e(ex);
        }
    }

    /**
     * 显示进度加载框
     *
     * @param msgId 文字id
     */
    public void showLoadingDialog(String msg, int msgId) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        if (getActivity() == null) return;
        // 显示进度框
        if (null == mLoadingDialog) {
            mLoadingDialog = new CustomProgressDialog(getActivity(),
                    R.style.prompt_style, msg, 0);
        }

        try {
            mLoadingDialog.show();
        } catch (Exception ex) {
            // dialog依附的activity如果被销毁，会抛出改异常
            LogUtils.e(ex);
        }
    }

    /**
     * 隐藏加载框
     */
    public void dismissLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
    }
}
