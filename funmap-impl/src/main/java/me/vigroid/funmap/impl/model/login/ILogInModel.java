package me.vigroid.funmap.impl.model.login;

import io.reactivex.Observable;

/**
 * Created by yangv on 2/5/2018.
 */

public interface ILogInModel {
    Observable<String> requestLogin(String userName, String psw);
}
