package me.vigroid.funmap.core.camera;

import android.net.Uri;

/**
 * Created by vigroid on 10/26/17.
 * Store some intermediate value
 */

public final class CameraImageBean {

    private Uri mPath = null;

    private static final CameraImageBean INSTANCE = new CameraImageBean();

    public static CameraImageBean getInstance(){
        return INSTANCE;
    }

    public Uri getPath(){
        return mPath;
    }

    public void setPath(Uri path){
        this.mPath = path;
    }

}
