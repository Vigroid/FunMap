package me.vigroid.funmap.core.net.rx;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import me.vigroid.funmap.core.net.RestCreator;
import me.vigroid.funmap.core.ui.loader.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by vigroid on 10/2/17.
 */

//This class applies the principle of Builder design pattern

public class RxRestClientBuilder {


    private String mUrl=null;
    private static final Map<String,Object> PARAMS = RestCreator.getParams();
    private RequestBody mBody=null;
    private Context mContext=null;
    private LoaderStyle mLoaderStyle=null;
    private File mFile = null;

    RxRestClientBuilder(){

    }

    public final RxRestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RxRestClientBuilder params(WeakHashMap<String, Object> params){
        PARAMS.putAll(params);
        return this;
    }

    public final RxRestClientBuilder params(String key, Object value){
        this.PARAMS.put(key,value);
        return  this;
    }

    public final RxRestClientBuilder file(File file){
        this.mFile=file;
        return  this;
    }

    public final RxRestClientBuilder file(String file){
        this.mFile=new File(file);
        return  this;
    }
    public final RxRestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RxRestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final RxRestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }

    public final RxRestClient build(){
        return new RxRestClient(mUrl,PARAMS,
                mBody,mFile,mContext,mLoaderStyle);
    }
}
