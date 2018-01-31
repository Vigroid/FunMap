package me.vigroid.funmap.impl.lbs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import me.vigroid.funmap.core.fragments.PermissionCheckerDelegate;
import me.vigroid.funmap.core.lbs.IMapHandler;
import me.vigroid.funmap.core.utils.callback.CallbackManager;
import me.vigroid.funmap.core.utils.callback.CallbackType;
import me.vigroid.funmap.core.utils.callback.IGlobalCallback;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.bean.MarkerType;

/**
 * Created by yangv on 1/22/2018.
 * Class to handle and initial map
 */

public class MapHandler implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<MarkerBean>, ClusterManager.OnClusterItemClickListener<MarkerBean>, IMapHandler {

    private ClusterManager<MarkerBean> mClusterManager;
    private static final String TAG = MapHandler.class.getSimpleName();

    //Map related
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private PermissionCheckerDelegate mDelegate;
    private List<MarkerBean> mBeans;

    public MapHandler(PermissionCheckerDelegate delegate, List<MarkerBean> beans) {
        this.mDelegate = delegate;
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(delegate.getContext());
        this.mBeans = beans;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (googleMap == null) {
            return;
        }
        mClusterManager = new ClusterManager<>(mDelegate.getContext(), googleMap);
        mClusterManager.setRenderer(new MarkerRender(mDelegate, mMap, mClusterManager));
        googleMap.setOnCameraIdleListener(mClusterManager);

        mClusterManager.addItems(mBeans);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final MarkerOptions markerOpt = new MarkerOptions()
                        .position(latLng)
                        .title("New Marker");

                Marker currentMarker = mMap.addMarker(markerOpt);
                beginChooseDialog(currentMarker);

                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                            @Override
                            public void executeCallback(Uri args) {
                                Toast.makeText(mDelegate.getContext(), args.toString(), Toast.LENGTH_SHORT).show();
                                addIconMarker(args, "V", latLng);
                            }
                        });
            }
        });


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
            Toast.makeText(mDelegate.getContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void beginChooseDialog(final Marker marker) {

        final Dialog alertDialog = new Dialog(mDelegate.getContext());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_choose_action);
        final Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
        }
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
                        mDelegate.startCameraWithCheck();
                        marker.remove();
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

    // add custom marker to map using icon generator class
    private void addIconMarker(Uri args, CharSequence text, LatLng position) {
        //TODO change desc and type
        mBeans.add(new MarkerBean(new String[]{args.toString()}, text.toString(), "heihei", position.latitude, position.longitude, MarkerType.PICS_MARKER, 0, false, false));
        mClusterManager.clearItems();
        mClusterManager.addItems(mBeans);

        float currentZoom = mMap.getCameraPosition().zoom;
        float targetZoom = (currentZoom + 1 <= mMap.getMaxZoomLevel()) ? currentZoom + 1 : currentZoom - 1;

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    position, targetZoom));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public boolean onClusterItemClick(MarkerBean bean) {
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    bean.getPosition(), DEFAULT_ZOOM));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onClusterClick(Cluster<MarkerBean> cluster) {
        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
