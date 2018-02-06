package me.vigroid.funmap.impl.model.login;

import io.reactivex.Observable;
import me.vigroid.funmap.core.net.rx.RxRestClient;
import me.vigroid.funmap.impl.api.API;

/**
 * Created by yangv on 2/5/2018.
 */

public class LogInModelImpl implements ILogInModel {

    @Override
    public Observable<String> requestLogin(String userName, String psw) {
        return RxRestClient.builder()
                .url(API.LOGIN)
                .params("name", userName)
                .params("psw", psw)
                .build()
                .post();
    }
}
