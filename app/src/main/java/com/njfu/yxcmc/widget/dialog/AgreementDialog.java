package com.njfu.yxcmc.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.njfu.yxcmc.R;


/**
 * 用户协议
 */
public class AgreementDialog extends AlertDialog implements OnClickListener {

    private Context mContext = null;
    private MyBuilder mBuilder;
    private Callback mCallback;

    private Button btnLeftInMyAlert, btnRightInMyAlert;
    private TextView tvTitle, tvMessage;
    private TextView tvHyperLink;
    private View verticalDividerInBtns;

    public AgreementDialog(Context context, MyBuilder builder, Callback callback) {
        super(context);
        this.mContext = context;
        this.mBuilder = builder;
        this.mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_usage_agreement);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutParams p = getWindow().getAttributes();
        Display d = wm.getDefaultDisplay();
        Point sizePoint = new Point();
        d.getSize(sizePoint);
        // p.height = (int) (sizePoint.x * 0.8);
        p.width = (int) (sizePoint.x * 0.9);
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
        tvHyperLink = findViewById(R.id.tvHyperlink);
        tvHyperLink.setVisibility(View.INVISIBLE);
        tvHyperLink.getPaint().setUnderlineText(true);
    }

    private void initListener() {
        btnLeftInMyAlert.setOnClickListener(this);
        btnRightInMyAlert.setOnClickListener(this);
        tvHyperLink.setOnClickListener(this);
    }

    private void initData() {
        tvTitle.setText(mBuilder.getTitle());
        tvMessage.setText(mBuilder.getMessage());
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
        switch (v.getId()) {
            case R.id.btnLeftInMyAlert: {
                if (mCallback != null) {
                    mCallback.onCancelBtnClick(this);
                }
                break;
            }
            case R.id.btnRightInMyAlert: {
                if (mCallback != null) {
                    mCallback.onConfirmBtnClick(this);
                }
                break;
            }
            case R.id.tvHyperlink:{
                if (mCallback != null) {
                    mCallback.onTvHyperlinkClick(this);
                }
                break;
            }
            default:
                break;
        }

    }

    public interface Callback {
        void onCancelBtnClick(AgreementDialog dialog);

        void onConfirmBtnClick(AgreementDialog dialog);

        void onTvHyperlinkClick(AgreementDialog dialog);
    }

    public static class MyBuilder {
        private String title = "";
        private String message = "";
        private String leftBtnText = "";
        private String rightBtnText = "";

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
    }

}
