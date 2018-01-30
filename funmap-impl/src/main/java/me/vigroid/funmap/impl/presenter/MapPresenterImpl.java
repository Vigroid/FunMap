package me.vigroid.funmap.impl.presenter;

import java.util.List;

import me.vigroid.funmap.impl.bean.MarkerBean;
import me.vigroid.funmap.impl.model.IMapModel;
import me.vigroid.funmap.impl.model.MapModelImpl;
import me.vigroid.funmap.impl.view.IMapDelegateView;

/**
 * Created by yangv on 1/30/2018.
 * Impl for IMapPresenter
 */

public class MapPresenterImpl implements IMapPresenter {
    IMapDelegateView mapView;
    IMapModel mapModel;

    public MapPresenterImpl(IMapDelegateView mapView) {
        this.mapView = mapView;
        this.mapModel = new MapModelImpl();
    }

    @Override
    public void detachView() {
        mapView = null;
    }

    @Override
    public void loadMarkers() {
        mapView.refreshMarker(mapModel.fetchMarkers());
    }
}
