package me.vigroid.funmap.impl.view.login;

/**
 * Created by yangv on 2/5/2018.
 */

public interface ILogInView{
    void showLoader();

    void stopLoader();

    void showNetError();

    void showPswError();

    void showUserNameError();

    void popAndResult();
}
