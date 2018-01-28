package me.vigroid.funmap.impl.root.v;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;

import com.google.android.gms.maps.MapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.core.lbs.MapHandler;
import me.vigroid.funmap.core.recycler.MarkerAdapter;
import me.vigroid.funmap.core.recycler.MarkerBean;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.R2;

/**
 * Created by yangv on 1/22/2018.
 * The root element in the activy, contains a map and a slide up panel
 */

public class BaseMapDelegate extends FunMapDelegate {

    final String TAG = BaseMapDelegate.class.getSimpleName();
    //1 mile
    final int DEFAULT_RANGE = 50;
    final int MIN_RANGE = 1;
    final int MAX_RANGE = 100;

    MapHandler mMapHandler = null;

    private int mRange = DEFAULT_RANGE;

    MarkerAdapter mAdapter = null;

    @BindView(R2.id.maps_sl_discover)
    SlidingUpPanelLayout mSlideLayout = null;

    @BindView(R2.id.rv_marks)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.map)
    MapView mMapView = null;

    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout = null;

    @OnClick(R2.id.fab)
    void onClickFab() {
        mMapHandler.getCurrentLocation();
    }

    @OnClick(R2.id.btn_nav_toggle)
    void onClickToggle(){
        mDrawerLayout.openDrawer(Gravity.START);
    }

    @OnClick(R2.id.slide_filter)
    void onClickFilter(){
        showRangeFilterDialog();
    }

    @OnTextChanged(value = R2.id.et_near_marker, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onNearMarkerTextChanged(CharSequence query){
        if(mAdapter!=null)
            mAdapter.filterSearch(query);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_base;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        //TODO move to model level
        MarkerBean bean1 = new MarkerBean("https://cnet4.cbsistatic.com/img/I-2dhG3a_A2B-LPW1vtnnnynxjk=/830x467/2017/10/16/53715bdb-d189-4aae-8e8b-f48cbe5d9512/google-pixel-2-0304-013.jpg", "Title A",32.996645, -96.795427);
        MarkerBean bean2 = new MarkerBean("http://blogs-images.forbes.com/gordonkelly/files/2017/06/Screenshot-2017-06-11-at-21.36.02.png", "Title B", 32.996645, -96.785427);
        MarkerBean bean3 = new MarkerBean("https://cdn-images-1.medium.com/max/1600/1*LElUGGMInnIAl4QpKwuP1Q.png", "Title C", 32.986645, -96.795427);
        MarkerBean bean4 = new MarkerBean("https://cdn-images-1.medium.com/max/1600/1*LElUGGMInnIAl4QpKwuP1Q.png", "Title C", 32.998635, -96.795427);
        MarkerBean bean5 = new MarkerBean("https://cdn-images-1.medium.com/max/1600/1*LElUGGMInnIAl4QpKwuP1Q.png", "Title C", 32.989625, -96.795427);
        MarkerBean bean6 = new MarkerBean("http://blogs-images.forbes.com/gordonkelly/files/2017/06/Screenshot-2017-06-11-at-21.36.02.png", "Title B", 32.997645, -96.795427);



        List<MarkerBean> beans = new ArrayList<>();

        beans.add(bean1);
        beans.add(bean2);
        beans.add(bean3);
        beans.add(bean4);
        beans.add(bean5);
        beans.add(bean6);

        mMapHandler = new MapHandler(this, beans);

        mSlideLayout.setAnchorPoint(0.8548f);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MarkerAdapter(beans, this.getContext());

        mRecyclerView.setAdapter(mAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(mMapHandler);

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

    @Override
    public boolean onBackPressedSupport() {
        Log.d(TAG, "back");
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }else if (mSlideLayout != null &&
                (mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlideLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        }else {
            return super.onBackPressedSupport();
        }
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
        if (window!=null) window.setWindowAnimations(me.vigroid.funmap.core.R.style.anim_panel_up_from_bottom);

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
}
