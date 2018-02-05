package me.vigroid.funmap.impl.presenter;

import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.bean.MarkerType;
import me.vigroid.funmap.core.response.MarkersResponse;
import me.vigroid.funmap.impl.filter.MarkerFilterTypes;
import me.vigroid.funmap.impl.model.IMapModel;
import me.vigroid.funmap.impl.model.MapModelImpl;
import me.vigroid.funmap.impl.view.IMapDelegateView;
import me.vigroid.funmap.impl.view.IMarkerItemView;

/**
 * Created by yangv on 1/30/2018.
 * Impl for IMapPresenter
 */

public class MapPresenterImpl implements IMapPresenter {
    private IMapDelegateView mapView;
    private IMapModel mapModel;
    private List<MarkerBean> mBeans = null;
    private List<MarkerBean> mBeansFiltered = new ArrayList<>();

    public MapPresenterImpl(IMapDelegateView mapView) {
        this.mapView = mapView;
        this.mapModel = new MapModelImpl();
    }

    @Override
    public void loadMarkers() {
        mapModel.fetchMarkers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MarkersResponse, List<MarkerBean>>() {
                    @Override
                    public List<MarkerBean> apply(MarkersResponse markersResponse) throws Exception {
                        return markersResponse.markerBeans;
                    }
                })
                .subscribe(new MarkerSingleObserver());
    }

    @Override
    public void onBindMarkerViewAtPosition(IMarkerItemView holder, int position) {
        MarkerBean bean = mBeansFiltered.get(position);
        holder.setImg(bean.imgUris);
        holder.setTitle(bean.title);
    }

    @Override
    public void onClickListenerAtPosition(int position) {
        mapView.showMarkerDetailDelegate(mBeansFiltered.get(position));
    }

    @Override
    public void getMarkerAtPostion(int position) {
        mapView.showMarkerOnMap(mBeansFiltered.get(position));
    }

    @Override
    public int getMarkersCount() {
        return mBeansFiltered.size();
    }

    @Override
    public <T> void filterMarkers(MarkerFilterTypes types, T value) {
        switch (types) {
            case KEYWORD:
                if (value instanceof String)
                    filterKeywords((String) value);
                break;
            case PIC_ONLY:
                if (value instanceof Boolean)
                    filterMarkerType(MarkerType.PICS_MARKER, (Boolean) value);
                break;
            case EVENT_ONLY:
                if (value instanceof Boolean)
                    filterMarkerType(MarkerType.EVENT_MARKER, (Boolean) value);
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(ClusterManager<MarkerBean> clusterManager) {
        clusterManager.clearItems();
        if (mBeansFiltered != null)
            clusterManager.addItems(mBeansFiltered);
        mapView.refreshMarker();
    }

    @Override
    public void addPicMarkers(ClusterManager<MarkerBean> clusterManager, MarkerBean bean) {
        //TODO upload beans to server and get result
        if (mapModel.addPicMarker(bean) == null) {
            mBeans.add(bean);
            clusterManager.addItem(bean);
        }
        mapView.refreshRv();
        mapView.refreshMarker();
    }

    @Override
    public void detachView() {
        mapView = null;
    }

    private void filterKeywords(String filterString) {
        if (filterString.isEmpty()) {
            mBeansFiltered = mBeans;
        } else {
            List<MarkerBean> tempBeans = new ArrayList<>();
            for (MarkerBean bean : mBeans) {
                if (bean.title.toLowerCase().contains(filterString.toLowerCase())) {
                    tempBeans.add(bean);
                }
            }
            mBeansFiltered = tempBeans;
        }
        mapView.refreshRv();
    }

    private void filterMarkerType(int type, Boolean isFiltered) {
        if (!isFiltered) {
            mBeansFiltered = mBeans;
        } else {
            List<MarkerBean> tempBeans = new ArrayList<>();
            for (MarkerBean bean : mBeansFiltered) {
                if (bean.type == type) tempBeans.add(bean);
            }
            mBeansFiltered = tempBeans;
        }
        mapView.refreshRv();
    }

    private class MarkerSingleObserver implements SingleObserver<List<MarkerBean>> {

        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onSuccess(List<MarkerBean> markerBeans) {

            mBeans = markerBeans;
            mBeansFiltered = markerBeans;
            mapView.refreshRv();
            mapView.refreshMarker();
        }

        @Override
        public void onError(Throwable e) {
            mapView.showRefreshError();
        }
    }
}
