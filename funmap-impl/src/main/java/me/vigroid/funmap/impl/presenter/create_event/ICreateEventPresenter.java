package me.vigroid.funmap.impl.presenter.create_event;

import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.impl.base.IPresenter;

/**
 * Created by yangv on 2/3/2018.
 * For p level
 */

public interface ICreateEventPresenter extends IPresenter{
    void addEventMarker(MarkerBean bean, boolean isPrivate);
}
