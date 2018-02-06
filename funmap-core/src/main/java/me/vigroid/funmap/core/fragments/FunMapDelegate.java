package me.vigroid.funmap.core.fragments;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import me.vigroid.funmap.core.R;
import me.vigroid.funmap.core.ui.loader.FunMapLoader;
import me.vigroid.funmap.core.ui.loader.LoaderStyle;

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
