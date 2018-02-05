package me.vigroid.funmap.impl.model.create_pic;

import io.reactivex.Observable;
import me.vigroid.funmap.core.bean.MarkerBean;

/**
 * Created by yangv on 2/4/2018.
 */

public interface ICreatePicModel {
    Observable<String> uploadPicMarker(MarkerBean bean);
}
