package me.vigroid.funmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vigroid.funmap.core.activity.ProxyActivity;
import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.impl.root.BaseMapDelegate;

public class MapsActivity extends ProxyActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        IRequestPermission{

    private static final String TAG = MapsActivity.class.getSimpleName();
    private final int PERMISSION_REQUEST_FINE_LOC = 0;

    private MapHandling mMapHandling = null;

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

    @BindView(R2.id.maps_sl_discover)
    SlidingUpPanelLayout mSlideLayout = null;

    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout = null;

    @BindView(R2.id.list)
    ListView lv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);
        FunMap.getConfigurator().withActivity(this);
        initView();

        if (savedInstanceState == null) {
            //load the root fragment into our container
            loadRootFragment(me.vigroid.funmap.core.R.id.delegate_container, new BaseMapDelegate());
        }
    }

    private void initView() {
        //mMapHandling = new MapHandling(this);
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(mMapHandling);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        toggle.syncState();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MapsActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mSlideLayout != null &&
                (mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlideLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOC);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOC:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //mMapHandling.getCurrentLocation();
                } else {
                    Toast.makeText(this, R.string.activity_permission_location_denied, Toast.LENGTH_LONG).show();
                }
        }
    }

}
