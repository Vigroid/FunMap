package me.vigroid.funmap.impl.model.register;

import io.reactivex.Observable;
import me.vigroid.funmap.core.net.rx.RxRestClient;
import me.vigroid.funmap.impl.api.API;

/**
 * Created by yangv on 2/5/2018.
 */

public class RegisterModelImpl implements IRegisterModel {
    @Override
    public Observable<String> requestRegister(String userName, String eMail, String psw) {
        return RxRestClient.builder()
                .url(API.REGISTER)
                .params("userName", userName)
                .params("email", eMail)
                .params("psw", psw)
                .build()
                .post();
    }
}
