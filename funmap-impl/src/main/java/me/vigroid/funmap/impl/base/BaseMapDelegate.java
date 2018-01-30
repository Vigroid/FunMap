package me.vigroid.funmap.impl.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;

import butterknife.BindView;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.impl.lbs.MapHandler;
import me.vigroid.funmap.impl.R2;

/**
 * Created by yangv on 1/30/2018.
 */

public abstract class BaseMapDelegate extends FunMapDelegate{

    final String TAG = this.getClass().getSimpleName();

    @BindView(R2.id.map)
    public MapView mMapView = null;

    public MapHandler mMapHandler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMapView.onCreate(savedInstanceState);
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
    public void onDestroyView() {
        mMapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
