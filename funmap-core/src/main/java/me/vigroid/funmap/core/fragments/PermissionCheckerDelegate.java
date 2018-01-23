package me.vigroid.funmap.core.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import me.vigroid.funmap.core.R;
import me.vigroid.funmap.core.lbs.MapHandling;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by vigroid on 10/12/17.
 * Add a permission check layer
 */
@RuntimePermissions
public abstract class PermissionCheckerDelegate extends BaseDelegate {

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void getCurrentLocation(MapHandling mapHandling){
        mapHandling.getCurrentLocation();
    }

    public void getCurrentLocationWithCheck(MapHandling mapHandling){
        PermissionCheckerDelegatePermissionsDispatcher.getCurrentLocationWithPermissionCheck(this,mapHandling);
    }

    //TODO rationale text
    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this.getContext())
                .setMessage("1")
                .setPositiveButton("2", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton("3", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedForCamera() {
        Toast.makeText(this.getContext(), "Denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void showNeverAskForCamera() {
        Toast.makeText(this.getContext(), "Never asked", Toast.LENGTH_SHORT).show();
    }
}
