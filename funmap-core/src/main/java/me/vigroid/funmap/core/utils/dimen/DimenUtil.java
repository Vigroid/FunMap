package me.vigroid.funmap.core.utils.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import me.vigroid.funmap.core.app.FunMap;

/**
 * Created by vigroid on 10/3/17.
 * Get the width and height of screen
 */

public class DimenUtil {
    public static int getScreenWidth(){
        final Resources resources = FunMap.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(){
        final Resources resources = FunMap.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
