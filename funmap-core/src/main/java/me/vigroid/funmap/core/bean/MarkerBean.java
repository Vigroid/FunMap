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
    public boolean isSaved;
    public String[] imgUris;
    public String userName;
    public String iconUri;
    public long unixTime;

    public MarkerBean(int ownerId, double lat, double lng, String title, String description, int type, boolean isSaved, String[] imgUris, String userName, String iconUri, long unixTime) {
        this.ownerId = ownerId;
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
        this.type = type;
        this.isSaved = isSaved;
        this.imgUris = imgUris;
        this.userName = userName;
        this.iconUri = iconUri;
        this.unixTime = unixTime;
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
