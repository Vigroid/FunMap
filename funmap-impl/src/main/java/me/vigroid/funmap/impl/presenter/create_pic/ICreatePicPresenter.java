package me.vigroid.funmap.impl.presenter.create_pic;

import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.impl.base.IPresenter;

/**
 * Created by yangv on 2/4/2018.
 */

public interface ICreatePicPresenter extends IPresenter{
    void addPicMarker(MarkerBean bean, boolean isPrivate);
}
