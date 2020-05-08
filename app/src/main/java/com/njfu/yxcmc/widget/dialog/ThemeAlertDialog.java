package com.njfu.yxcmc.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.njfu.yxcmc.R;


public class ThemeAlertDialog extends AlertDialog implements OnClickListener {

    private MyBuilder mBuilder;
    private Callback mCallback;

    private Button btnLeftInMyAlert, btnRightInMyAlert;
    private TextView tvTitle, tvMessage;
    private View verticalDividerInBtns;

    public interface OnAlertAction {
        void onLeftClick();

        void onRightClick();
    }

    public static void showAlert(Activity activity, String alertContent) {
        showAlert(activity, "提示", alertContent, false, "确定", null, null);
    }

    public static void showAlert(@NonNull Activity activity, @StringRes int titleStrId, @StringRes int contentStringId, boolean isCancelable, @StringRes int leftBtnStrId, @StringRes int rightBtnStrId, final OnAlertAction listener) {
        showAlert(activity, activity.getString(titleStrId), activity.getString(contentStringId), isCancelable, activity.getString(leftBtnStrId), activity.getString(rightBtnStrId), listener);
    }

    public static void showAlert(Activity activity, String title, String content, boolean isCancelable, String leftBtnStr, String rightBtnStr, final OnAlertAction listener) {
        if (activity == null || activity.isFinishing()) return;
        if (TextUtils.isEmpty(leftBtnStr) && TextUtils.isEmpty(rightBtnStr)) return;
        ThemeAlertDialog.MyBuilder builder = new ThemeAlertDialog.MyBuilder(title, content, leftBtnStr, rightBtnStr);
        ThemeAlertDialog dialog = new ThemeAlertDialog(activity, builder, new ThemeAlertDialog.Callback() {
            @Override
            public void onLeftBtnClick(ThemeAlertDialog dialog) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onLeftClick();
                }
            }

            @Override
            public void onRightBtnClick(ThemeAlertDialog dialog) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onRightClick();
                }
            }
        });
        dialog.setCancelable(isCancelable);
        dialog.show();
    }

    public ThemeAlertDialog(Context context, MyBuilder builder, Callback callback) {
        super(context);
        this.mBuilder = builder;
        this.mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_theme_alert);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        LayoutParams p = getWindow().getAttributes();
        Display d = wm.getDefaultDisplay();
        Point sizePoint = new Point();
        d.getSize(sizePoint);
        // p.height = (int) (sizePoint.x * 0.8);
        p.width = (int) ((sizePoint.y > sizePoint.x ? sizePoint.x : sizePoint.y) * 0.8);
        p.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//        p.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        p.gravity = Gravity.CENTER;
        getWindow().setAttributes(p); // 设置生效
        initView();
        initData();
        initListener();
        this.setCancelable(false);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitleInMyAlert);
        tvMessage = (TextView) findViewById(R.id.tvMessageInMyAlert);
        btnLeftInMyAlert = (Button) findViewById(R.id.btnLeftInMyAlert);
        btnRightInMyAlert = (Button) findViewById(R.id.btnRightInMyAlert);
        verticalDividerInBtns = findViewById(R.id.verticalDividerInBtns);

        tvMessage.setMovementMethod(new ScrollingMovementMethod());
    }

    private void initListener() {
        btnLeftInMyAlert.setOnClickListener(this);
        btnRightInMyAlert.setOnClickListener(this);
    }

    private void initData() {
        tvTitle.setText(mBuilder.getTitle());
        tvMessage.setText(mBuilder.getMessage());
        tvMessage.setGravity(mBuilder.getMsgContentGravity());
        if (TextUtils.isEmpty(mBuilder.getLeftBtnText())) {
            btnLeftInMyAlert.setVisibility(View.GONE);
            verticalDividerInBtns.setVisibility(View.GONE);
        } else {
            btnLeftInMyAlert.setVisibility(View.VISIBLE);
            btnLeftInMyAlert.setText(mBuilder.getLeftBtnText());
        }
        if (TextUtils.isEmpty(mBuilder.getRightBtnText())) {
            btnRightInMyAlert.setVisibility(View.GONE);
            verticalDividerInBtns.setVisibility(View.GONE);
        } else {
            btnRightInMyAlert.setVisibility(View.VISIBLE);
            btnRightInMyAlert.setText(mBuilder.getRightBtnText());
        }
        if (TextUtils.isEmpty(mBuilder.getLeftBtnText()) && !TextUtils.isEmpty(mBuilder.getRightBtnText())) {
            btnRightInMyAlert.setBackgroundResource(R.drawable.theme_dialog_btn_one_bg);
        } else if (!TextUtils.isEmpty(mBuilder.getLeftBtnText()) && TextUtils.isEmpty(mBuilder.getRightBtnText())) {
            btnLeftInMyAlert.setBackgroundResource(R.drawable.theme_dialog_btn_one_bg);
        }

        tvMessage.setTextIsSelectable(mBuilder.isMessageTextSelectable());
    }

    /**
     * 刷新视图，主要用来刷新title、message
     *
     * @param builder
     */
    public void refreshView(MyBuilder builder) {
        mBuilder = builder;
        initData();
    }

    @Override
    public void onClick(View v) {
        // Library 中不能switch-case到R.id
        if (btnLeftInMyAlert == v) {
            if (mCallback != null) {
                mCallback.onLeftBtnClick(this);
            }
        } else if (btnRightInMyAlert == v) {
            if (mCallback != null) {
                mCallback.onRightBtnClick(this);
            }
        }
    }

    public void setOnTitleClickListener(View.OnClickListener listener) {
        tvTitle.setOnClickListener(listener);
    }

    public interface Callback {
        void onLeftBtnClick(ThemeAlertDialog dialog);

        void onRightBtnClick(ThemeAlertDialog dialog);
    }

    public static class MyBuilder {
        private String title = "";
        private String message = "";
        private String leftBtnText = "";
        private String rightBtnText = "";
        private boolean isMessageTextSelectable = false;
        private int msgContentGravity = Gravity.CENTER;

        public MyBuilder() {
        }

        public MyBuilder(String title, String message, String leftBtnText, String rightBtnText) {
            setTitle(title);
            setMessage(message);
            setLeftBtnText(leftBtnText);
            setRightBtnText(rightBtnText);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getLeftBtnText() {
            return leftBtnText;
        }

        public void setLeftBtnText(String leftBtnText) {
            this.leftBtnText = leftBtnText;
        }

        public String getRightBtnText() {
            return rightBtnText;
        }

        public void setRightBtnText(String rightBtnText) {
            this.rightBtnText = rightBtnText;
        }

        public void setMessageTextSelectable(boolean messageTextSelectable) {
            isMessageTextSelectable = messageTextSelectable;
        }

        public boolean isMessageTextSelectable() {
            return isMessageTextSelectable;
        }

        public int getMsgContentGravity() {
            return msgContentGravity;
        }

        public void setMsgContentGravity(int msgContentGravity) {
            this.msgContentGravity = msgContentGravity;
        }
    }

}
