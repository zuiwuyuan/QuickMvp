package com.njfu.yxcmc.module.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.njfu.yxcmc.R;
import com.njfu.yxcmc.base.BaseMvpActivity;
import com.njfu.yxcmc.base.GlobalApp;
import com.njfu.yxcmc.bean.UserModel;
import com.njfu.yxcmc.common.CmssSharedPreferences;
import com.njfu.yxcmc.common.GlobalField;
import com.njfu.yxcmc.contract.LoginContract;
import com.njfu.yxcmc.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.etPhoneNum)
    EditText etPhoneNum;
    @BindView(R.id.imgPhoneNumDelete)
    ImageView imgPhoneNumDelete;
    @BindView(R.id.etIdCardCode)
    EditText etIdCardCode;
    @BindView(R.id.imgIdCardCodeDelete)
    ImageView imgIdCardCodeDelete;

    private CmssSharedPreferences mSharedPref;

    private GlobalField myGlobalField;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initImmersionBar() {

        ImmersionBar.with(this)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();  //必须调用方可应用以上所配置的参数
    }

    @Override
    public void initView() {

        TextWatcher etWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //switchLoginBtnEnable();
                imgPhoneNumDelete.setVisibility(TextUtils.isEmpty(etPhoneNum.getText()) ? View.INVISIBLE : View.VISIBLE);
                imgIdCardCodeDelete.setVisibility(TextUtils.isEmpty(etIdCardCode.getText()) ? View.INVISIBLE : View.VISIBLE);
            }
        };

        etPhoneNum.addTextChangedListener(etWatcher);
        etIdCardCode.addTextChangedListener(etWatcher);

    }

    @Override
    public void initData() {

        etPhoneNum.setText("zuiwuyuan");
        etIdCardCode.setText("123456");

        mSharedPref = GlobalApp.getInstance().getPrefs();
        myGlobalField = GlobalApp.getInstance().getGlobalField();


        mPresenter = new LoginPresenter(this);
        mPresenter.attachView(this);
    }

    @OnClick({R.id.tvBtnLogin, R.id.imgPhoneNumDelete
            , R.id.imgIdCardCodeDelete})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tvBtnLogin: {
                requestLogin();
                break;
            }

            case R.id.imgPhoneNumDelete: {
                etPhoneNum.setText("");
                break;
            }
            case R.id.imgIdCardCodeDelete: {
                etIdCardCode.setText("");
                break;
            }

        }

    }

    private void requestLogin() {

        String username = etPhoneNum.getText().toString().trim();
        String password = etIdCardCode.getText().toString().trim();

        mPresenter.login(username, password);
    }

    @Override
    public void onLoginSuccess(UserModel userModel) {
        toastMsg("登录成功");
    }

}
