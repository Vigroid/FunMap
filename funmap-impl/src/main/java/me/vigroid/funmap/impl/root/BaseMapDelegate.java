package me.vigroid.funmap.impl.root;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.core.lbs.MapHandling;
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

    List<String> your_array_list = Arrays.asList(
            "This",
            "Is",
            "An",
            "Example",
            "ListView",
            "That",
            "You",
            "Can",
            "Scroll",
            ".",
            "It",
            "Shows",
            "How",
            "Any",
            "Scrollable",
            "View",
            "Can",
            "Be",
            "Included",
            "As",
            "A",
            "Child",
            "Of",
            "SlidingUpPanelLayout"
    );

    MapHandling mMapHandling = null;


    private int mRange = DEFAULT_RANGE;

    @BindView(R2.id.maps_sl_discover)
    SlidingUpPanelLayout mSlideLayout = null;

    @BindView(R2.id.list)
    ListView lv = null;

    @BindView(R2.id.map)
    MapView mMapView = null;

    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout = null;

    @OnClick(R2.id.fab)
    void onClickFab() {
        mMapHandling.getCurrentLocation();
    }

    @OnClick(R2.id.btn_nav_toggle)
    void onClickToggle(){
        mDrawerLayout.openDrawer(Gravity.START);
    }

    @OnClick(R2.id.slide_filter)
    void onClickFilter(){
        showRangeFilterDialog();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_base;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        mMapHandling = new MapHandling(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BaseMapDelegate.this.getContext(), "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(mMapHandling);

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
