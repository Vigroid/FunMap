package me.vigroid.funmap.impl.model;

import io.reactivex.Observable;
import io.reactivex.Single;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.response.MarkersResponse;

/**
 * Created by yangv on 1/30/2018.
 * Skeleton for model impl
 */

public interface IMapModel {

    Single<MarkersResponse> fetchMarkers();

    Observable<String> addPicMarker(MarkerBean bean);

}
