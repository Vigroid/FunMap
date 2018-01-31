package me.vigroid.funmap.impl.view;

import java.util.List;

import me.vigroid.funmap.core.bean.MarkerBean;

/**
 * Created by yangv on 1/30/2018.
 * View layer for map delegate, an interface
 */

public interface IMapDelegateView{
    void refreshMarker(List<MarkerBean> beans);
    void showRefreshError();
}
