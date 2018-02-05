package me.vigroid.funmap.impl.view.main;

import java.util.List;

import me.vigroid.funmap.core.bean.MarkerBean;

/**
 * Created by yangv on 1/30/2018.
 * View layer for map delegate, an interface
 */

public interface IMapDelegateView {

    void refreshMarker();

    void refreshRv();

    void showMarkerDetailDelegate(MarkerBean bean);

    void showMarkerOnMap(MarkerBean bean);

    void showRefreshError();
}
