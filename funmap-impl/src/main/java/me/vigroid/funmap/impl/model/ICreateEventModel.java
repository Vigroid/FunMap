package me.vigroid.funmap.impl.model;


import io.reactivex.Observable;
import me.vigroid.funmap.core.bean.MarkerBean;

/**
 * Created by yangv on 2/3/2018.
 */

public interface ICreateEventModel {
    Observable<String> uploadEventMarker(MarkerBean bean);
}

