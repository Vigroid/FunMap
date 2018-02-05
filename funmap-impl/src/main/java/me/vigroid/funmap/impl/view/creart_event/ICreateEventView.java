package me.vigroid.funmap.impl.view.creart_event;

import me.vigroid.funmap.core.bean.MarkerBean;

/**
 * Created by yangv on 2/3/2018.
 */

public interface ICreateEventView {
    void showLoader();

    void stopLoader();

    void showError();

    void popAndResult(MarkerBean bean);
}
