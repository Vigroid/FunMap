package me.vigroid.funmap.impl.view.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.util.Patterns;
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
import me.vigroid.funmap.impl.presenter.register.IRegisterPresenter;
import me.vigroid.funmap.impl.presenter.register.RegisterPresenterImpl;

/**
 * Created by yangv on 2/5/2018.
 */

public class RegisterDelegate extends FunMapDelegate implements IRegisterView {

    private final String TAG = this.getClass().getSimpleName();
    private final int MIN_PSW_LEN = 6;

    IRegisterPresenter mPresenter = new RegisterPresenterImpl(this);

    @BindView(R2.id.edit_sign_up_name)
    TextInputEditText mName = null;
    @BindView(R2.id.edit_sign_up_email)
    TextInputEditText mEmail = null;
    @BindView(R2.id.edit_sign_up_password)
    TextInputEditText mPassword = null;
    @BindView(R2.id.edit_sign_up_re_password)
    TextInputEditText mRePassword = null;

    @OnClick(R2.id.btn_sign_up)
    void onClickSignUp() {
        if (checkForm())
            popAndResult();
            //TODO dummy first
            //mPresenter.register(mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_register;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

    }

    private boolean checkForm() {
        final String name = mName.getText().toString();
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        final String rePassword = mRePassword.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            mName.setError("Please input name");
            isPass = false;
        } else {
            mName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Wrong Email Address");
            isPass = false;
        } else {
            mEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("Length is at least 6 digits");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || !(rePassword.equals(password))) {
            mRePassword.setError("Please reenter the password");
            isPass = false;
        } else {
            mRePassword.setError(null);
        }

        return isPass;
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
    public void showEmailExist() {
        Toast.makeText(_mActivity, R.string.net_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserNameExist() {
        Toast.makeText(_mActivity, R.string.net_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void popAndResult() {
        Bundle bundle = new Bundle();
        Toast.makeText(_mActivity, String.format(getString(R.string.notify_log_in), FunMap.getConfiguration(ConfigKeys.USER_NAME)), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "username logged in - " + FunMap.getConfiguration(ConfigKeys.USER_NAME));
        setFragmentResult(RESULT_OK, bundle);
        //TODO popTo
        pop();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
