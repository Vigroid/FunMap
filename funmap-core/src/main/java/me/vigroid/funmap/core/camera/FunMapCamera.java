package me.vigroid.funmap.core.camera;

import android.net.Uri;

import me.vigroid.funmap.core.fragments.PermissionCheckerDelegate;
import me.vigroid.funmap.core.utils.file.FileUtil;


/**
 * Created by vigroid on 10/26/17.
 * Camera Invoke
 */

public class FunMapCamera {

    public static Uri createCropFile(){
        return Uri.parse
                (FileUtil.createFile("crop_image",
                        FileUtil.getFileNameByTime("IMG","jpg")).getPath());
    }

    public static void start(PermissionCheckerDelegate delegate){
        new CameraHandler(delegate).takePhoto();
    }

    public static void startDialog(PermissionCheckerDelegate delegate){
        new CameraHandler(delegate).beginCameraDialog();
    }
}
