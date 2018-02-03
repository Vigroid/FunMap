package me.vigroid.funmap.impl.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import butterknife.BindView;
import io.reactivex.subjects.PublishSubject;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.adapter.MarkerAdapter;
import me.vigroid.funmap.impl.lbs.MapHandler;
import me.vigroid.funmap.impl.R2;
import me.vigroid.funmap.impl.presenter.IMapPresenter;
import me.vigroid.funmap.impl.presenter.MapPresenterImpl;
import me.vigroid.funmap.impl.view.IMapDelegateView;

/**
 * Created by yangv on 1/30/2018.
 */

public abstract class BaseMapDelegate extends FunMapDelegate {

    final String TAG = this.getClass().getSimpleName();

    @BindView(R2.id.map)
    public MapView mMapView = null;

    protected PublishSubject<String> mPublishSubject = PublishSubject.create();

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
        mMapView.onStop();
        super.onStop();
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
