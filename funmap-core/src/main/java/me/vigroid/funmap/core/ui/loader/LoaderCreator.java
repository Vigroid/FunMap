package me.vigroid.funmap.core.ui.loader;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * Created by vigroid on 10/2/17.
 * createor for loader
 */

public final class LoaderCreator {

    //A buffer stores the loading map we used before, this improves the performance
    private static final WeakHashMap<String,Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView create(String type, Context context){

        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (LOADING_MAP.get(type) == null){
            final Indicator indicator =getIndicator(type);
            LOADING_MAP.put(type,indicator);
        }

        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;

    }

    private static Indicator getIndicator(String name){
        if (name == null||name.isEmpty()){
            return null;
        }
        final StringBuilder drawableClasssName = new StringBuilder();
        if(!name.contains(".")){
            final String defaultPackageName = AVLoadingIndicatorView.class.getPackage().getName();
            drawableClasssName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");
        }
        drawableClasssName.append(name);
        try {
            final Class<?> drawableClass = Class.forName(drawableClasssName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
