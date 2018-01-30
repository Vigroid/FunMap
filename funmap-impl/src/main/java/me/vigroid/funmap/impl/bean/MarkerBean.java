package me.vigroid.funmap.impl.bean;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

/**
 * Created by yangv on 1/25/2018.
 * Bean for marker, used for recycler view
 */

public class MarkerBean implements ClusterItem {
    private String[] imgUri;
    private String title;
    private String description;
    private LatLng latLng;
    private int type;
    private int ownerId;
    private boolean isLiked;
    private boolean isSaved;
    //TODO expire time

    public MarkerBean(String[] imgUri, String title, String description, double lat, double lng, int type, int ownerId, boolean isLiked, boolean isSaved) {
        this.imgUri = imgUri;
        this.title = title;
        this.description = description;
        this.latLng = new LatLng(lat, lng);
        this.type = type;
        this.ownerId = ownerId;
        this.isLiked = isLiked;
        this.isSaved = isSaved;
    }

    public MarkerBean(String[] imgUri, String title, String description, LatLng latLng, int type, int ownerId, boolean isLiked, boolean isSaved) {
        this.imgUri = imgUri;
        this.title = title;
        this.description = description;
        this.latLng = latLng;
        this.type = type;
        this.ownerId = ownerId;
        this.isLiked = isLiked;
        this.isSaved = isSaved;
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

    public String[] getImgUri() {
        return imgUri;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getType() {
        return type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isSaved() {
        return isSaved;
    }
}
