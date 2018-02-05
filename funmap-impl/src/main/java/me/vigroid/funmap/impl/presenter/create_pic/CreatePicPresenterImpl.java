package me.vigroid.funmap.impl.presenter.create_pic;

import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.impl.model.create_pic.CreatePicModelImpl;
import me.vigroid.funmap.impl.model.create_pic.ICreatePicModel;
import me.vigroid.funmap.impl.view.create_pic.ICreatePicView;

/**
 * Created by yangv on 2/4/2018.
 */

public class CreatePicPresenterImpl implements ICreatePicPresenter {

    private ICreatePicView picView;
    private ICreatePicModel picModel;
    private MarkerBean mBean;

    public CreatePicPresenterImpl(ICreatePicView picView) {
        this.picView = picView;
        this.picModel = new CreatePicModelImpl();
    }

    @Override
    public void addPicMarker(MarkerBean bean, boolean isPrivate) {
        mBean = bean;
        picView.popAndResult(mBean);
    }

    @Override
    public void detachView() {
        picView = null;
    }
}
