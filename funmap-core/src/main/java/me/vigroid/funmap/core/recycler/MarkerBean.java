package me.vigroid.funmap.core.recycler;

/**
 * Created by yangv on 1/25/2018.
 * Bean for marker, used for recycler view
 */

public class MarkerBean {
    String imgUri;
    String title;

    public MarkerBean(String imgUri, String title) {
        this.imgUri = imgUri;
        this.title = title;
    }
}
