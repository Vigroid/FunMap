package me.vigroid.funmap.impl.model;

import io.reactivex.Observable;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.net.rx.RxRestClient;

/**
 * Created by yangv on 2/3/2018.
 */

public class CreatEventModelImpl implements ICreateEventModel {
    @Override
    public Observable<String> uploadEventMarker(MarkerBean bean) {
        return RxRestClient.builder()
                .url(" ")
                .file(" ")
                .build()
                .upload();
    }
}
