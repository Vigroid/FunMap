package me.vigroid.funmap.impl.view.main;

/**
 * Created by yangv on 1/31/2018.
 * View interface for marker recyclerview item
 * This keeps the consistency of MVP design(Recyclerview and its adapter in MVP)
 */

public interface IMarkerItemView {

    void setImg(String[] imgUris);

    void setTitle(String title);
}
