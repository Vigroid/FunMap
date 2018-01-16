package me.vigroid.funmap.core.fragments;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by vigroid on 10/12/17.
 * A skeleton delegate class
 */

public abstract class FunMapDelegate extends PermissionCheckerDelegate{
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public <T extends FunMapDelegate> T getParentDelegate(){
        return (T) getParentFragment();
    }
}
