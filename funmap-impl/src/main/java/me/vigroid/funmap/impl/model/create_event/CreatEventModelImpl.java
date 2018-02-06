package me.vigroid.funmap.impl.model.create_event;

import io.reactivex.Observable;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.net.rx.RxRestClient;
import me.vigroid.funmap.impl.api.API;

/**
 * Created by yangv on 2/3/2018.
 */

public class CreatEventModelImpl implements ICreateEventModel {
    @Override
    public Observable<String> uploadEventMarker(MarkerBean bean) {
        return RxRestClient.builder()
                .url(API.CREATE_MARKER)
                .file(" ")
                .build()
                .upload();
    }
}
