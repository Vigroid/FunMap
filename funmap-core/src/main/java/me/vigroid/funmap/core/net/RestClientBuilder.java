package me.vigroid.funmap.core.net;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import me.vigroid.funmap.core.net.callback.IError;
import me.vigroid.funmap.core.net.callback.IFailure;
import me.vigroid.funmap.core.net.callback.IRequest;
import me.vigroid.funmap.core.net.callback.ISuccess;
import me.vigroid.funmap.core.ui.loader.LoaderStyle;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by vigroid on 10/2/17.
 */

//This class applies the principle of Builder design pattern

public class RestClientBuilder {


    private String mUrl=null;
    private static final Map<String,Object> PARAMS = RestCreator.getParams();
    private IRequest mIRequest=null;
    private ISuccess mISuccess=null;
    private IFailure mIFailure=null;
    private IError mIError=null;
    private RequestBody mBody=null;
    private Context mContext=null;
    private LoaderStyle mLoaderStyle=null;
    private File mFile = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;

    RestClientBuilder(){

    }

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params){
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value){
        this.PARAMS.put(key,value);
        return  this;
    }

    public final RestClientBuilder file(File file){
        this.mFile=file;
        return  this;
    }

    public final RestClientBuilder file(String file){
        this.mFile=new File(file);
        return  this;
    }

    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    public final RestClientBuilder dir(String dir) {
        this.mDownloadDir = dir;
        return this;
    }

    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest){
        this.mIRequest= iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccesss){
        this.mISuccess= iSuccesss;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure= iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError){
        this.mIError= iError;
        return this;
    }

    public final RestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final RestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }

    public final RestClient build(){
        return new RestClient(mUrl,PARAMS,
                mIRequest, mExtension, mName,
                mDownloadDir,mISuccess,mIFailure,mIError,
                mBody,mFile,mContext,mLoaderStyle);
    }
}
