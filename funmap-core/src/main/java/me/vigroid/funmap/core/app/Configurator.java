package me.vigroid.funmap.core.app;

import android.app.Activity;
import android.os.Handler;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * Created by yangv on 1/13/2018.
 * A singleton that created when app started, it got a hashmap
 * that stores all the runtime global configurations we needed
 */

public class Configurator {
    //config info
    private static final HashMap<Object,Object> FUN_MAP_CONFIGS = new HashMap<>();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();

    private Configurator(){
        //set default values for configs
        FUN_MAP_CONFIGS.put(ConfigKeys.CONFIG_READY,false);
        FUN_MAP_CONFIGS.put(ConfigKeys.API_HOST, "");
        FUN_MAP_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        FUN_MAP_CONFIGS.put(ConfigKeys.SIGNED_IN, false);
        FUN_MAP_CONFIGS.put(ConfigKeys.USER_ID, -1);
        FUN_MAP_CONFIGS.put(ConfigKeys.USER_ICON_URI, "");
        FUN_MAP_CONFIGS.put(ConfigKeys.USER_NAME, "");
    }

    //holder class for the singleton
    private static class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }

    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }

    //Get the configuration Hashmap
    final HashMap<Object,Object> getFunMapConfigs(){
        return FUN_MAP_CONFIGS;
    }

    //set host IP
    public final Configurator withApiHost(String host){
        FUN_MAP_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }
    //Pass some Interceptor(Network)
    public final Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        FUN_MAP_CONFIGS.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        FUN_MAP_CONFIGS.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    public final Configurator withActivity(Activity activity){
        FUN_MAP_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }
    //Pass some IconFontDescriptor to add support for those icons
    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }

    public final Configurator withSignIn(boolean isSignIn){
        FUN_MAP_CONFIGS.put(ConfigKeys.SIGNED_IN, isSignIn);
        return this;
    }

    public final Configurator withUserID(int userID){
        FUN_MAP_CONFIGS.put(ConfigKeys.USER_ID, userID);
        return this;
    }

    public final Configurator withUserName(String userName){
        FUN_MAP_CONFIGS.put(ConfigKeys.USER_NAME, userName);
        return this;
    }

    public final Configurator withUserIcon(String userIcon){
        FUN_MAP_CONFIGS.put(ConfigKeys.USER_ICON_URI, userIcon);
        return this;
    }

    private void initIcons(){
        if (ICONS.size()>0){
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i<ICONS.size();i++){
                initializer.with(ICONS.get(i));
            }
        }
    }

    public final void configure(){
        initIcons();
        //set the configure to ready state
        FUN_MAP_CONFIGS.put(ConfigKeys.CONFIG_READY,true);
    }

    private void checkConfiguration(){
        final boolean isReady = (boolean) FUN_MAP_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady){
            throw new RuntimeException("The configuration is not ready!");
        }
    }

    //get each configuration detail
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key){
        checkConfiguration();
        //get the configuration value by key
        final Object value = FUN_MAP_CONFIGS.get(key);
        if (value == null){
            throw new NullPointerException(key.toString() + ": This configuration is null");
        }
        return (T) value;
    }
}
