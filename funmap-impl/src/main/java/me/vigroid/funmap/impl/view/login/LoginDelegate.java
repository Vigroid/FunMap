package me.vigroid.funmap.impl.view.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import me.vigroid.funmap.core.app.ConfigKeys;
import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.core.ui.loader.FunMapLoader;
import me.vigroid.funmap.core.ui.loader.LoaderStyle;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.R2;
import me.vigroid.funmap.impl.presenter.login.ILogInPresenter;
import me.vigroid.funmap.impl.presenter.login.LogInPresenterImpl;
import me.vigroid.funmap.impl.view.register.RegisterDelegate;

/**
 * Created by yangv on 2/5/2018.
 * Login page, v level
 */

public class LoginDelegate extends FunMapDelegate implements ILogInView {

    private final String TAG = this.getClass().getSimpleName();

    ILogInPresenter mPresenter = new LogInPresenterImpl(this);

    @BindView(R2.id.edit_sign_in_username)
    TextInputEditText mUserName = null;

    @BindView(R2.id.edit_sign_in_password)
    TextInputEditText mPassword = null;

    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {
        if (checkForm())
            popAndResult();
            //TODO dummy first, net latter
            //mPresenter.logIn(mUserName.getText().toString(), mPassword.getText().toString());
    }

    @OnClick(R2.id.tv_link_sign_up)
    void onClickLink(){
        start(new RegisterDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_login;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

    }

    private boolean checkForm() {
        int MIN_NAME_LEN = 4;
        int MIN_PSW_LEN = 6;

        final String username = mUserName.getText().toString();
        final String password = mPassword.getText().toString();

        boolean isPass = true;


        if (username.isEmpty() || username.length() < MIN_NAME_LEN) {
            mUserName.setError(String.format(getString(R.string.user_less_than), MIN_NAME_LEN));
            isPass = false;
        } else {
            mUserName.setError(null);
        }

        if (password.isEmpty() || password.length() < MIN_PSW_LEN) {
            mPassword.setError(String.format(getString(R.string.psw_less_than), MIN_NAME_LEN));
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        return isPass;
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showLoader() {
        FunMapLoader.showLoading(_mActivity, LoaderStyle.BallSpinFadeLoaderIndicator.name());
    }

    @Override
    public void stopLoader() {
        FunMapLoader.stopLoading();
    }

    @Override
    public void showNetError() {
        Toast.makeText(_mActivity, R.string.net_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPswError() {
        Toast.makeText(_mActivity, R.string.login_psw_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserNameError() {
        Toast.makeText(_mActivity, R.string.login_username_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void popAndResult() {
        Bundle bundle = new Bundle();
        Toast.makeText(_mActivity, String.format(getString(R.string.notify_log_in),FunMap.getConfiguration(ConfigKeys.USER_NAME)), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "username logged in - " + FunMap.getConfiguration(ConfigKeys.USER_NAME));
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }
}
