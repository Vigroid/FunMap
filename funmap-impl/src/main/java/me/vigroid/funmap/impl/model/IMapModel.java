package me.vigroid.funmap.impl.model;

import io.reactivex.Single;
import me.vigroid.funmap.core.response.MarkerResponse;

/**
 * Created by yangv on 1/30/2018.
 * Skeleton for model impl
 */

public interface IMapModel {

    Single<MarkerResponse> fetchMarkers();
}
