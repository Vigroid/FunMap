package me.vigroid.funmap.core.utils.callback;

import io.reactivex.annotations.Nullable;

/**
 * Created by vigroid on 10/27/17.
 */

public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);
}
