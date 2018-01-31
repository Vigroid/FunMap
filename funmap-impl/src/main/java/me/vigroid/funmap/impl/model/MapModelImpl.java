package me.vigroid.funmap.impl.model;

import io.reactivex.Single;
import me.vigroid.funmap.core.net.rx.RxRestClient;
import me.vigroid.funmap.core.response.MarkerResponse;

/**
 * Created by yangv on 1/30/2018.
 * Impl for IMapModel, used to fetch data from server
 */

public class MapModelImpl implements IMapModel {

    @Override
    public Single<MarkerResponse> fetchMarkers() {

        return RxRestClient.builder()
                .url("testMarkers.php")
                .build()
                .getMarkers();

    }
}
