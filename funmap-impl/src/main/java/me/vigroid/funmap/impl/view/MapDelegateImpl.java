package me.vigroid.funmap.impl.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.vigroid.funmap.impl.lbs.MapHandler;
import me.vigroid.funmap.impl.adapter.MarkerAdapter;
import me.vigroid.funmap.impl.bean.MarkerBean;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.R2;
import me.vigroid.funmap.impl.base.BaseMapDelegate;
import me.vigroid.funmap.impl.presenter.IMapPresenter;
import me.vigroid.funmap.impl.presenter.MapPresenterImpl;

/**
 * Created by yangv on 1/22/2018.
 * The root element in the activy, contains a map and a slide up panel
 */

public class MapDelegateImpl extends BaseMapDelegate implements IMapDelegateView{

    final String TAG = MapDelegateImpl.class.getSimpleName();

    final int DEFAULT_RANGE = 50;
    final int MIN_RANGE = 1;
    final int MAX_RANGE = 100;
    final float ANCHOR_POINT = 0.8548f;

    IMapPresenter mPresenter;
    MapHandler mMapHandler = null;
    int mRange = DEFAULT_RANGE;
    List<MarkerBean> mBeans = new ArrayList<>();
    MarkerAdapter mAdapter = null;

    @BindView(R2.id.maps_sl_discover)
    SlidingUpPanelLayout mSlideLayout = null;

    @BindView(R2.id.rv_marks)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout = null;

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
    void onNearMarkerTextChanged(CharSequence query) {
        if (mAdapter != null) mAdapter.filterSearch(query);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_map;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        mMapHandler = new MapHandler(this, mBeans);
        mMapView.getMapAsync(mMapHandler);

        mSlideLayout.setAnchorPoint(ANCHOR_POINT);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MarkerAdapter(mBeans, this.getContext());
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new MapPresenterImpl(this);
        mPresenter.loadMarkers();
    }

    @Override
    public void refreshMarker(List<MarkerBean> beans) {
        mBeans.clear();
        mBeans.addAll(beans);
        mAdapter.notifyDataSetChanged();
    }

    private void showRangeFilterDialog() {
        final Dialog filterDialog = new Dialog(this.getContext());
        filterDialog.setContentView(R.layout.dialog_range_filter);
        final NumberPicker mNp = filterDialog.findViewById(R.id.np_range);
        mNp.setMaxValue(MAX_RANGE);
        mNp.setMinValue(MIN_RANGE);
        mNp.setValue(mRange);
        mNp.setWrapSelectorWheel(false);
        final Window window = filterDialog.getWindow();
        if (window != null)
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);

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
    }

    @Override
    public boolean onBackPressedSupport() {
        Log.d(TAG, "back");
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (mSlideLayout != null &&
                (mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlideLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        } else {
            return super.onBackPressedSupport();
        }
    }

}
