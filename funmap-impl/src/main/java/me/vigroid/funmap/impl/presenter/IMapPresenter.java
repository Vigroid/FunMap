package me.vigroid.funmap.impl.presenter;

import me.vigroid.funmap.impl.base.IPresenter;
import me.vigroid.funmap.impl.view.IMapDelegateView;

/**
 * Created by yangv on 1/30/2018.
 * Presenter for map delegate, a bridge
 */

public interface IMapPresenter extends IPresenter<IMapDelegateView>{

    void loadMarkers();
}
