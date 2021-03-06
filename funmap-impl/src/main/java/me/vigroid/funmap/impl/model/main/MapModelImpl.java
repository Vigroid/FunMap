package me.vigroid.funmap.impl.model.main;

import io.reactivex.Observable;
import io.reactivex.Single;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.net.rx.RxRestClient;
import me.vigroid.funmap.core.response.MarkersResponse;
import me.vigroid.funmap.impl.api.API;

/**
 * Created by yangv on 1/30/2018.
 * Impl for IMapModel, used to fetch data from server
 */

public class MapModelImpl implements IMapModel {

    @Override
    public Single<MarkersResponse> fetchMarkers() {

        return RxRestClient.builder()
                .url(API.FETCH_MARKERS)
                .build()
                .getMarkers();

    }
}
