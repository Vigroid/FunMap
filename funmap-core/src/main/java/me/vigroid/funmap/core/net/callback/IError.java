package me.vigroid.funmap.core.net.callback;

/**
 * Created by vigroid on 10/2/17.
 * Callback when error happens
 */

public interface IError {

    void onError(int code, String msg);
}
