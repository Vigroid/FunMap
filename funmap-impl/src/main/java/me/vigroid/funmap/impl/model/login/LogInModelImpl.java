package me.vigroid.funmap.impl.model.login;

import io.reactivex.Observable;
import me.vigroid.funmap.core.net.rx.RxRestClient;

/**
 * Created by yangv on 2/5/2018.
 */

public class LogInModelImpl implements ILogInModel {

    @Override
    public Observable<String> requestLogin(String userName, String psw) {
        return RxRestClient.builder()
                .url("login.php")
                .params("name", userName)
                .params("psw", psw)
                .build()
                .post();
    }
}
