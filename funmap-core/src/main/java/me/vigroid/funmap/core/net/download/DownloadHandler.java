package me.vigroid.funmap.core.net.download;

import android.os.AsyncTask;

import java.util.WeakHashMap;

import me.vigroid.funmap.core.net.RestCreator;
import me.vigroid.funmap.core.net.callback.IError;
import me.vigroid.funmap.core.net.callback.IFailure;
import me.vigroid.funmap.core.net.callback.IRequest;
import me.vigroid.funmap.core.net.callback.ISuccess;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vigroid on 10/3/17.
 * download handler
 */

public class DownloadHandler {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    //文件下载目录
    private final String DOWNLOAD_DIR;
    //后缀
    private final String EXTENSION;
    //file name
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownloadHandler(String url,
                           IRequest request,
                           String downloadDir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error) {
        this.URL = url;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
    }

    public void handleDownload() {
        if (REQUEST != null) {
            //started downloading
            REQUEST.onRequestEnd();
        }

        RestCreator.getRestService().download(URL, PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            final ResponseBody responseBody = response.body();

                            final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS);
                            task.executeOnExecutor(
                                    AsyncTask.THREAD_POOL_EXECUTOR, DOWNLOAD_DIR, EXTENSION, responseBody, NAME);

                            //check if download completed
                            if (task.isCancelled()) {
                                if (REQUEST != null) {
                                    //this will end the HTTP connection
                                    REQUEST.onRequestEnd();
                                }
                            }
                        }else {
                            if (ERROR!=null){
                                ERROR.onError(response.code(),response.message());
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (FAILURE!=null){
                            FAILURE.onFailure();
                        }
                    }
                });
    }
}
