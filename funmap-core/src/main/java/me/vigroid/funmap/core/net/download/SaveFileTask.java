package me.vigroid.funmap.core.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.io.InputStream;

import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.core.net.callback.IRequest;
import me.vigroid.funmap.core.net.callback.ISuccess;
import me.vigroid.funmap.core.utils.file.FileUtil;
import okhttp3.ResponseBody;

/**
 * Created by vigroid on 10/3/17.
 */

public class SaveFileTask extends AsyncTask<Object,Void,File> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    public SaveFileTask(IRequest request, ISuccess success) {
        this.REQUEST = request;
        this.SUCCESS = success;
    }

    @Override
    protected File doInBackground(Object... objects) {

        String downloadDir = (String) objects[0];
        String extension = (String) objects[1];
        final ResponseBody body = (ResponseBody) objects[2];
        final String name = (String)objects[3];
        final InputStream is = body.byteStream();
        if(downloadDir == null||downloadDir.equals("")){
            downloadDir = "down_loads";
        }
        if (extension ==null||extension.equals("")){
            extension = "";
        }
        if (name == null){
            //generate a name
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        } else {
            return FileUtil.writeToDisk(is,downloadDir,name);
        }

    }

    //back to main thread

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(SUCCESS!=null){
            SUCCESS.onSuccess(file.getPath());
        }
        if ((REQUEST!=null)){
            REQUEST.onRequestEnd();
        }
    }

    //Auto update
    private void autoInstallApk(File file){
        if (FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            FunMap.getApplicationContext().startActivity(install);
        }
    }
}
