package me.vigroid.funmap.impl.lbs;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import me.vigroid.funmap.core.fragments.PermissionCheckerDelegate;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.core.bean.MarkerBean;

/**
 * Created by yangv on 1/27/2018.
 * Render the icon markers on map and their cluster behavior
 */

public class MarkerRender extends DefaultClusterRenderer<MarkerBean> {

    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
    private final ImageView mImageView;
    private final int mDimension;
    private final Context mContext;
    private Bitmap icon;

    public MarkerRender(PermissionCheckerDelegate delegate, GoogleMap map, ClusterManager<MarkerBean> clusterManager) {
        super(delegate.getContext(), map, clusterManager);
        this.mContext = delegate.getContext();

        this.mIconGenerator  = new IconGenerator(mContext);
        this.mClusterIconGenerator = new IconGenerator(mContext);

        View multiProfile = delegate.getLayoutInflater().inflate(R.layout.multi_profile, null);
        mClusterIconGenerator.setContentView(multiProfile);

        mImageView = new ImageView(mContext);
        mDimension = (int) delegate.getResources().getDimension(R.dimen.custom_profile_image);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        int padding = (int) delegate.getResources().getDimension(R.dimen.custom_profile_padding);
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);
    }


    @Override
    protected void onBeforeClusterItemRendered(MarkerBean bean, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
        icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(bean.getTitle());
    }

    @Override
    protected void onClusterItemRendered(MarkerBean clusterItem, final Marker marker) {

        //TODO, event or pic diff color, change imguri to iconuri(get from ownid)
        String[] iconUris = clusterItem.imgUri;

        if(iconUris.length == 0) {
            mImageView.setImageResource(R.mipmap.ic_default_icon);
            icon=mIconGenerator.makeIcon();
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            return;
        }

        Glide.with(mContext)
                .load(iconUris[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .thumbnail(0.1f)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mImageView.setImageDrawable(resource);
                        icon = mIconGenerator.makeIcon();
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                    }
                });
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 3;
    }
}