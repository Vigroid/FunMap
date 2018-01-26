package me.vigroid.funmap.core.lbs;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import me.vigroid.funmap.core.R;
import me.vigroid.funmap.core.fragments.PermissionCheckerDelegate;
import me.vigroid.funmap.core.utils.callback.CallbackManager;
import me.vigroid.funmap.core.utils.callback.CallbackType;
import me.vigroid.funmap.core.utils.callback.IGlobalCallback;

/**
 * Created by yangv on 1/22/2018.
 * Class to handle and initial map
 */

public class MapHandler implements OnMapReadyCallback {

    private static final String TAG = MapHandler.class.getSimpleName();

    //Map related
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private PermissionCheckerDelegate mDelegate;

    public MapHandler(PermissionCheckerDelegate delegate) {
        this.mDelegate = delegate;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(delegate.getContext());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (googleMap != null) {
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
                                    IconGenerator iconGenerator = new IconGenerator(mDelegate.getContext());
                                    iconGenerator.setTextAppearance(R.style.iconGenText);
                                    iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
                                    addIcon(iconGenerator, "V", latLng);
                                }
                            });
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
    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        mMap.addMarker(markerOptions);
    }
}
