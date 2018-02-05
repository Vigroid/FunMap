package me.vigroid.funmap.impl.presenter.main;

import com.google.maps.android.clustering.ClusterManager;

import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.impl.base.IPresenter;
import me.vigroid.funmap.impl.filter.MarkerFilterTypes;
import me.vigroid.funmap.impl.view.main.IMarkerItemView;

/**
 * Created by yangv on 1/30/2018.
 * Presenter for map delegate, a bridge
 */

public interface IMapPresenter extends IPresenter {

    void onMapReady(ClusterManager<MarkerBean> clusterManager);

    void addMarker(ClusterManager<MarkerBean> clusterManager, MarkerBean bean);

    void loadMarkers();

    void onBindMarkerViewAtPosition(IMarkerItemView itemView, int position);

    void onClickListenerAtPosition(int position);

    void getMarkerAtPostion(int position);

    int getMarkersCount();

    <T> void filterMarkers(MarkerFilterTypes types, T value);
}
