package me.vigroid.funmap.impl.presenter.create_event;

import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.impl.model.create_event.CreatEventModelImpl;
import me.vigroid.funmap.impl.model.create_event.ICreateEventModel;
import me.vigroid.funmap.impl.view.creart_event.ICreateEventView;

/**
 * Created by yangv on 2/3/2018.
 * Impl of ICreateEventPresenter, p level
 */

public class CreateEventPresenterImpl implements ICreateEventPresenter {

    private ICreateEventView eventView;
    private ICreateEventModel eventModel;
    private MarkerBean mBean;

    public CreateEventPresenterImpl(ICreateEventView view) {
        this.eventView = view;
        this.eventModel = new CreatEventModelImpl();
    }

    @Override
    public void addEventMarker(final MarkerBean bean, boolean isPrivate) {
        mBean = bean;
        eventView.popAndResult(mBean);
        //TODO implment real upload
//        eventModel.uploadEventMarker(bean)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        eventView.showLoader();
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        if (s == null || s.isEmpty())
//                            eventView.showError();
//                        else
//                            eventView.popAndResult(bean);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        eventView.showError();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        eventView.stopLoader();
//                    }
//                });
    }

    @Override
    public void detachView() {
        eventView = null;
    }
}
