package me.vigroid.funmap.impl.view.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import me.vigroid.funmap.core.app.ConfigKeys;
import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.core.ui.loader.FunMapLoader;
import me.vigroid.funmap.core.ui.loader.LoaderStyle;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.presenter.login.ILogInPresenter;
import me.vigroid.funmap.impl.presenter.login.LogInPresenterImpl;

/**
 * Created by yangv on 2/5/2018.
 */

public class LoginDelegate extends FunMapDelegate implements ILogInView {

    ILogInPresenter mPresenter = new LogInPresenterImpl(this);

    @Override
    public Object setLayout() {
        return R.layout.delegate_login;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

    }

    private boolean checkForm(){
        return true;
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
        //TODO localization
        Toast.makeText(_mActivity, "Signed in as" + FunMap.getConfiguration(ConfigKeys.USER_NAME), Toast.LENGTH_SHORT).show();
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }
}
