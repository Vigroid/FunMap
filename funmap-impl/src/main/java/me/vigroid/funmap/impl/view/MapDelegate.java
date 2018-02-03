package me.vigroid.funmap.impl.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import me.vigroid.funmap.core.lbs.IMapHandler;
import me.vigroid.funmap.core.utils.dimen.DimenUtil;
import me.vigroid.funmap.impl.adapter.MarkerAdapter;
import me.vigroid.funmap.impl.filter.MarkerFilterTypes;
import me.vigroid.funmap.impl.lbs.MapHandler;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.R2;
import me.vigroid.funmap.impl.base.BaseMapDelegate;
import me.vigroid.funmap.impl.presenter.IMapPresenter;
import me.vigroid.funmap.impl.presenter.MapPresenterImpl;

/**
 * Created by yangv on 1/22/2018.
 * The root element in the activy, contains a map and a slide up panel
 */

public class MapDelegate extends BaseMapDelegate implements IMapDelegateView {

    final String TAG = MapDelegate.class.getSimpleName();

    final int DEFAULT_RANGE = 50;
    final int MIN_RANGE = 1;
    final int MAX_RANGE = 100;
    final float ANCHOR_POINT = 0.8548f;
    final int DEBOUNCE_INTERVAL = 400;

    int mRange = DEFAULT_RANGE;
    MarkerAdapter mAdapter = null;
    int savedPosition = 0;

    @BindView(R2.id.maps_sl_discover)
    SlidingUpPanelLayout mSlideLayout = null;

    @BindView(R2.id.rv_marks)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout = null;
    private IMapPresenter mPresenter = new MapPresenterImpl(this);
    private IMapHandler mMapHandler;

    @OnClick(R2.id.fab)
    void onClickFab() {
        mMapHandler.getCurrentLocation();
    }

    @OnClick(R2.id.btn_nav_toggle)
    void onClickToggle() {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    @OnClick(R2.id.slide_filter)
    void onClickFilter() {
        showRangeFilterDialog();
    }

    @OnTextChanged(value = R2.id.et_near_marker, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onMarkerSearchTextChanged(CharSequence query) {
        mPublishSubject.onNext(query.toString());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_map;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        mPresenter.loadMarkers();
        mMapHandler = new MapHandler(this, mPresenter);
        mMapView.getMapAsync(mMapHandler);

        mSlideLayout.setAnchorPoint(ANCHOR_POINT);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.HORIZONTAL));
        mAdapter = new MarkerAdapter(this.getContext(), mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int currPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && currPosition >= 0 && currPosition != savedPosition) {
                    mPresenter.getMarkerAtPostion(currPosition);
                    savedPosition = currPosition;
                }
            }
        });

        mPublishSubject.debounce(DEBOUNCE_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mPresenter.filterMarkers(MarkerFilterTypes.KEYWORD, s);
                    }
                });
    }

    @Override
    public void refreshMarker() {
        mMapHandler.notifyMarkersChanged();
    }

    @Override
    public void refreshRv() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMarkerDetailDelegate(MarkerBean bean) {
        //TODO start marker detail delegate
        Toast.makeText(this.getContext(), bean.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMarkerOnMap(MarkerBean bean) {
        mMapHandler.animateCamera(bean.getPosition());
    }

    @Override
    public void showRefreshError() {
        Toast.makeText(this.getContext(), R.string.refresh_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    private void showRangeFilterDialog() {
        final Dialog filterDialog = new Dialog(this.getContext(), R.style.dialog);
        filterDialog.setContentView(R.layout.dialog_range_filter);
        final NumberPicker mNp = filterDialog.findViewById(R.id.np_range);
        mNp.setMaxValue(MAX_RANGE);
        mNp.setMinValue(MIN_RANGE);
        mNp.setValue(mRange);
        mNp.setWrapSelectorWheel(false);
        filterDialog.findViewById(R.id.btn_np_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filterDialog.dismiss();
                    }
                });
        filterDialog.findViewById(R.id.btn_np_set)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRange = mNp.getValue();
                        Log.d(TAG, String.valueOf(mRange));
                        filterDialog.dismiss();
                    }
                });
        filterDialog.show();
        if (filterDialog.getWindow() != null)
            filterDialog.getWindow().setLayout((6 * DimenUtil.getScreenWidth()) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (mSlideLayout != null &&
                (mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlideLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        } else{
            return super.onBackPressedSupport();
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == MapHandler.ON_CREATE_EVENT && resultCode == RESULT_OK && data != null) {
            Toast.makeText(this.getContext(), data.getString(MapHandler.KEY_RESULT_BEAN), Toast.LENGTH_SHORT).show();
        }
    }
}
