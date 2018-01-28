package me.vigroid.funmap.core.recycler;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by yangv on 1/25/2018.
 * Bean for marker, used for recycler view
 */

public class MarkerBean implements ClusterItem{
    String imgUri;
    String title;
    LatLng latLng;

    public MarkerBean(String imgUri, String title, double lat, double lng) {
        this.imgUri = imgUri;
        this.title = title;
        this.latLng = new LatLng(lat, lng);
    }

    public MarkerBean(String imgUri, String title, LatLng latLng) {
        this.imgUri = imgUri;
        this.title = title;
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getImgUri() {
        return imgUri;
    }
}
