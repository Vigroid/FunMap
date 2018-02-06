package me.vigroid.funmap.impl.model.register;

import io.reactivex.Observable;

/**
 * Created by yangv on 2/5/2018.
 */

public interface IRegisterModel {
    Observable<String> requestRegister(String userName, String eMail, String psw);
}
