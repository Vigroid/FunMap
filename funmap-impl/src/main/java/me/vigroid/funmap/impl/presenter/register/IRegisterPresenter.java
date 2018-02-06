package me.vigroid.funmap.impl.presenter.register;

import me.vigroid.funmap.impl.base.IPresenter;

/**
 * Created by yangv on 2/5/2018.
 */

public interface IRegisterPresenter extends IPresenter {
    void register(String userName, String eMail, String psw);
}
