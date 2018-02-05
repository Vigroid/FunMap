package me.vigroid.funmap.core.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by yangv on 1/25/2018.
 * Bean for marker, used for recycler view
 */

public class MarkerBean implements ClusterItem, Parcelable {

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

    /**
     * Parcelable part
     *
     * @param in
     */

    public static final  Parcelable.Creator<MarkerBean> CREATOR = new Creator<MarkerBean>() {
        @Override
        public MarkerBean createFromParcel(Parcel parcel) {
            return new MarkerBean(parcel);
        }

        @Override
        public MarkerBean[] newArray(int i) {
            return new MarkerBean[i];
        }
    };

    private MarkerBean(Parcel in) {
        this.ownerId = in.readInt();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readInt();
        this.isSaved = in.readByte() != 0;
        this.imgUris = in.createStringArray();
        this.userName = in.readString();
        this.iconUri = in.readString();
        this.unixTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.ownerId);
        parcel.writeDouble(this.lat);
        parcel.writeDouble(this.lng);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeInt(this.type);
        parcel.writeByte((byte) (this.isSaved ? 1 : 0));
        parcel.writeStringArray(this.imgUris);
        parcel.writeString(this.userName);
        parcel.writeString(this.iconUri);
        parcel.writeLong(this.unixTime);
    }
}
