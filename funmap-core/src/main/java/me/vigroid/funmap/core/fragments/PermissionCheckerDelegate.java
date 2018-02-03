package me.vigroid.funmap.core.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import me.vigroid.funmap.core.R;
import me.vigroid.funmap.core.camera.CameraImageBean;
import me.vigroid.funmap.core.camera.FunMapCamera;
import me.vigroid.funmap.core.camera.RequestCodes;
import me.vigroid.funmap.core.lbs.IMapHandler;
import me.vigroid.funmap.core.utils.callback.CallbackManager;
import me.vigroid.funmap.core.utils.callback.CallbackType;
import me.vigroid.funmap.core.utils.callback.IGlobalCallback;
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


    //not real code for camera start, just for annotation process
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startCamera() {
        FunMapCamera.start(this);
    }

    public void startCameraWithCheck() {
        PermissionCheckerDelegatePermissionsDispatcher.startCameraWithPermissionCheck(this);
    }

    //not real code for camera start, just for annotation process
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startCameraDialog() {
        FunMapCamera.startDialog(this);
    }

    public void startCameraDialogWithCheck(){
        PermissionCheckerDelegatePermissionsDispatcher.startCameraDialogWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void getCurrentLocation(IMapHandler mapHandler) {
        mapHandler.getCurrentLocation();
    }

    public void getCurrentLocationWithCheck(IMapHandler mapHandler) {
        PermissionCheckerDelegatePermissionsDispatcher.getCurrentLocationWithPermissionCheck(this, mapHandler);
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(final PermissionRequest request) {
        new AlertDialog.Builder(this.getContext())
                .setMessage(R.string.rational_location_message)
                .setPositiveButton(R.string.rational_agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.rational_disagree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedForLocation() {
        Toast.makeText(this.getContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void showNeverAskForLocation() {
        Toast.makeText(this.getContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraDenied() {
        Toast.makeText(getContext(), R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraNever() {
        Toast.makeText(getContext(), R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraRationale(PermissionRequest request) {
        showRationaleDialog(request);
    }

    private void showRationaleDialog(final PermissionRequest request) {
        new android.support.v7.app.AlertDialog.Builder(getContext())
                .setMessage(R.string.rational_camera_write_message)
                .setPositiveButton(R.string.rational_agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.rational_disagree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheckerDelegatePermissionsDispatcher.
                onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.TAKE_PHOTO:
                    final Uri resultUri = CameraImageBean.getInstance().getPath();
                    //first arg source path, second arg crop result path
                    UCrop.of(resultUri, resultUri)
                            .withMaxResultSize(1080, 1920)
                            .start(getContext(), this);
                    break;
                case RequestCodes.PICK_PHOTO:
                    if (data!=null){
                        final Uri pickPath = data.getData();
                        //a path to store the cropped image
                        final String pickCropPath = FunMapCamera.createCropFile().getPath();
                        UCrop.of(pickPath,Uri.parse(pickCropPath))
                                .withMaxResultSize(400,400)
                                .start(getContext(),this);
                    }
                    break;
                case RequestCodes.CROP_PHOTO:
                    final Uri cropUri = UCrop.getOutput(data);
                    // get cropped data
                    @SuppressWarnings("unchecked") final IGlobalCallback<Uri> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);
                    if (callback != null) {
                        callback.executeCallback(cropUri);
                    }
                    break;
                case RequestCodes.CROP_ERROR:
                    Toast.makeText(getContext(), R.string.cropping_error, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
