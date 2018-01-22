package me.vigroid.funmap.impl.root;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.OnClick;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.impl.EventDelegate;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.R2;

/**
 * Created by yangv on 1/22/2018.
 */

public class BaseMapDelegate extends FunMapDelegate implements OnMapReadyCallback {

    @BindView(R2.id.map)
    MapView mMapView = null;

    @OnClick(R2.id.fab)
    void onClickFab(){
        start(new EventDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_base;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
