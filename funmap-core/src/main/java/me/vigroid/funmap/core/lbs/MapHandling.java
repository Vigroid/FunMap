package me.vigroid.funmap.core.lbs;

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

import me.vigroid.funmap.core.R;
import me.vigroid.funmap.core.app.ConfigKeys;
import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.core.fragments.PermissionCheckerDelegate;

/**
 * Created by yangv on 1/22/2018.
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
    private PermissionCheckerDelegate mDelegate;

    public MapHandling(PermissionCheckerDelegate delegate) {
        this.mDelegate = delegate;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(delegate.getContext());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

        mDelegate.getCurrentLocationWithCheck(this);
    }

    public void getCurrentLocation() {
        if (mMap == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(mDelegate.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(mDelegate.getActivity(), new OnCompleteListener<Location>() {
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
            //TODO local
            Toast.makeText(mDelegate.getContext(), "Denied", Toast.LENGTH_LONG).show();
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void beginChooseDialog(final Marker marker) {

        final Dialog alertDialog = new Dialog(mDelegate.getContext());
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
                        //TODO start camera, if succ, change icon
                        alertDialog.dismiss();
                    }
                });
        alertDialog
                .findViewById(R.id.btn_dialog_event)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO start event delegate, if succ, change icon
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
