package me.vigroid.funmap.impl.presenter.login;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.vigroid.funmap.impl.model.login.ILogInModel;
import me.vigroid.funmap.impl.model.login.LogInModelImpl;
import me.vigroid.funmap.impl.view.login.ILogInView;

/**
 * Created by yangv on 2/5/2018.
 */

public class LogInPresenterImpl implements ILogInPresenter {

    private ILogInView logInView;
    private ILogInModel logInModel;

    public LogInPresenterImpl(ILogInView logInView) {
        this.logInView = logInView;
        logInModel = new LogInModelImpl();
    }

    @Override
    public void detachView() {
        logInView = null;
    }

    @Override
    public void logIn(String userName, String psw) {
        logInModel.requestLogin(userName,psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        logInView.showLoader();
                    }

                    @Override
                    public void onNext(String s) {
                        //TODO my logic, maybe code int
                        if (s.equals("suc")){
                            logInView.stopLoader();
                            logInView.popAndResult();
                        }else if (s.equals("user_psw")){
                            logInView.showUserNameError();
                        }else {
                            logInView.showPswError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        logInView.showNetError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
