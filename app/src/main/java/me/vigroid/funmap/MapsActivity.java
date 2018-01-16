package me.vigroid.funmap;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.SupportMapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vigroid.funmap.core.activity.ProxyActivity;
import me.vigroid.funmap.core.app.FunMap;

public class MapsActivity extends ProxyActivity{

    @BindView(R2.id.toolbar)
    Toolbar toolbar = null;

    @BindView(R2.id.drawer_layout)
    DrawerLayout drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hideActionBarNStatusBar();
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        FunMap.init(this)
                .withApiHost("https://httpbin.org/")
                .withActivity(this)
                .configure();

        initView();
    }

    private void initView(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MapInit());

        //TODO: 1. change toogle style and size
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
