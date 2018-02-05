package me.vigroid.funmap.impl.model;

import io.reactivex.Observable;
import io.reactivex.Single;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.net.rx.RxRestClient;
import me.vigroid.funmap.core.response.MarkersResponse;

/**
 * Created by yangv on 1/30/2018.
 * Impl for IMapModel, used to fetch data from server
 */

public class MapModelImpl implements IMapModel {

    @Override
    public Single<MarkersResponse> fetchMarkers() {

        return RxRestClient.builder()
                .url("testMarkers.php")
                .build()
                .getMarkers();

    }

    @Override
    public Observable<String> addPicMarker(MarkerBean bean) {
//        return RxRestClient.builder()
//                .url("upload")
//                .build()
//                .upload();
        return null;
    }
}
