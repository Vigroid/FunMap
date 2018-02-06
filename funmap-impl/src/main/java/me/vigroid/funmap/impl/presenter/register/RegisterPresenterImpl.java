package me.vigroid.funmap.impl.presenter.register;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.vigroid.funmap.impl.model.register.IRegisterModel;
import me.vigroid.funmap.impl.model.register.RegisterModelImpl;
import me.vigroid.funmap.impl.view.register.IRegisterView;

/**
 * Created by yangv on 2/5/2018.
 */

public class RegisterPresenterImpl implements IRegisterPresenter {

    private IRegisterView registerView;
    private IRegisterModel registerModel;

    public RegisterPresenterImpl(IRegisterView view){
        this.registerView = view;
        this.registerModel = new RegisterModelImpl();
    }

    @Override
    public void register(String userName,String eMail, String psw) {
        registerModel
                .requestRegister(userName,eMail,psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        registerView.showLoader();
                    }

                    @Override
                    public void onNext(String s) {
                        registerView.stopLoader();
                        registerView.popAndResult();
                    }

                    @Override
                    public void onError(Throwable e) {
                        registerView.showNetError();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void detachView() {
        registerView = null;
    }
}
