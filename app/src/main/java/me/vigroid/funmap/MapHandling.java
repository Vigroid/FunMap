package me.vigroid.funmap;

import android.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import me.vigroid.funmap.core.activity.ProxyActivity;
import me.vigroid.funmap.core.app.ConfigKeys;
import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.impl.EventDelegate;

/**
 * Created by yangv on 1/21/2018.
 * Class to handle and initial map
 */

public class MapHandling implements OnMapReadyCallback {

    private static final String TAG = MapHandling.class.getSimpleName();

    //Map related
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private IRequestPermission mActivity;

    MapHandling(IRequestPermission activity) {
        this.mActivity = activity;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) mActivity);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mActivity == null)
            mActivity = FunMap.getConfiguration(ConfigKeys.ACTIVITY);

        mActivity.requestPermission();
        getCurrentLocation();

        if (googleMap != null) {
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MarkerOptions markerOpt = new MarkerOptions()
                            .position(latLng)
                            .title("New Marker");

                    Marker marker = mMap.addMarker(markerOpt);
                    beginChooseDialog(marker);
                }
            });
        }
    }

    public void getCurrentLocation() {
        if (mMap == null) {
            return;
        }

        if (mActivity == null)
            mActivity = FunMap.getConfiguration(ConfigKeys.ACTIVITY);

        if (ActivityCompat.checkSelfPermission((Context) mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener((Activity) mActivity, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.animateCamera(CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                    }
                }
            });
        } else {
            Toast.makeText((Context) mActivity, R.string.activity_permission_location_denied, Toast.LENGTH_LONG).show();
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void beginChooseDialog(final Marker marker) {

        if (mActivity == null)
            mActivity = FunMap.getConfiguration(ConfigKeys.ACTIVITY);

        final Dialog alertDialog = new Dialog((Context) mActivity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_choose_action);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                marker.remove();
            }
        });
        alertDialog
                .findViewById(R.id.btn_dialog_camera)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //((ProxyActivity)mActivity).start(new EventDelegate());
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
