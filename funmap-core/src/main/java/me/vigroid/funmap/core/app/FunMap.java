package me.vigroid.funmap.core.app;

import android.content.Context;

/**
 * Created by yangv on 1/13/2018.
 * User set and get global configuration by using this class
 */

public final class FunMap {

    public static Configurator init(Context context){
        getConfigurator()
                .getFunMapConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        return getConfigurator();
    }

    public static Configurator getConfigurator(){
        return Configurator.getInstance();
    }

    //get a single configuration
    public static <T> T getConfiguration(Object key){
        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }
}
