package me.vigroid.funmap.impl.presenter;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.response.MarkerResponse;
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
    public void loadMarkers() {

        SingleObserver<List<MarkerBean>> singleObserver = new MarkerSingleObserver();

        mapModel.fetchMarkers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MarkerResponse, List<MarkerBean>>() {
                    @Override
                    public List<MarkerBean> apply(MarkerResponse markerResponse) throws Exception {
                        return markerResponse.markerBeans;
                    }
                })
                .subscribe(singleObserver);
    }

    private class MarkerSingleObserver implements SingleObserver<List<MarkerBean>>{

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(List<MarkerBean> markerBeans) {
            mapView.refreshMarker(markerBeans);
        }

        @Override
        public void onError(Throwable e) {
            mapView.showRefreshError();
        }
    }

    @Override
    public void detachView() {
        mapView = null;
    }
}
