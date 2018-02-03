package me.vigroid.funmap.core.lbs;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yangv on 1/30/2018.
 * Public interface for map related work
 */

public interface IMapHandler extends OnMapReadyCallback {
    void getCurrentLocation();

    void notifyMarkersChanged();

    void animateCamera(LatLng position);

    @Override
    void onMapReady(GoogleMap googleMap);
}
