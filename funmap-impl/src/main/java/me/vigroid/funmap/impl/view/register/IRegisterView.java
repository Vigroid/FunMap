package me.vigroid.funmap.impl.view.register;

/**
 * Created by yangv on 2/5/2018.
 */

public interface IRegisterView {
    void showLoader();

    void stopLoader();

    void showNetError();

    void showEmailExist();

    void showUserNameExist();

    void popAndResult();
}
