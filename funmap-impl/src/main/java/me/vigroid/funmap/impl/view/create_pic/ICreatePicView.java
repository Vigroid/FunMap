package me.vigroid.funmap.impl.view.create_pic;

import me.vigroid.funmap.core.bean.MarkerBean;

/**
 * Created by yangv on 2/4/2018.
 */

public interface ICreatePicView {
    void showLoader();

    void stopLoader();

    void showError();

    void popAndResult(MarkerBean bean);
}
