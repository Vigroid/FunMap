package me.vigroid.funmap.core.bean;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by yangv on 1/25/2018.
 * Bean for marker, used for recycler view
 */

public class MarkerBean implements ClusterItem {

    public int ownerId;
    public double lat;
    public double lng;
    public String title;
    public String description;
    public int type;
    public boolean isLiked;
    public boolean isSaved;
    public String[] imgUri;
    //TODO expire time

    public MarkerBean(String[] imgUri, String title, String description, double lat, double lng, int type, int ownerId, boolean isLiked, boolean isSaved) {
        this.imgUri = imgUri;
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.ownerId = ownerId;
        this.isLiked = isLiked;
        this.isSaved = isSaved;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
