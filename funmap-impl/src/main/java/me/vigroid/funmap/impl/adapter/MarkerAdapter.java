package me.vigroid.funmap.impl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import me.vigroid.funmap.core.utils.ui.ScrimUtil;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.presenter.main.IMapPresenter;
import me.vigroid.funmap.impl.view.main.IMarkerItemView;

/**
 * Created by yangv on 1/31/2018.
 * Adpater for rv
 */

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.MarkerHolder> {

    private Context mContext;
    private IMapPresenter mPresenter;

    public MarkerAdapter(Context mContext, IMapPresenter mPresenter) {
        this.mContext = mContext;
        this.mPresenter = mPresenter;
    }

    @Override
    public MarkerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_marker_item, parent, false);
        return new MarkerAdapter.MarkerHolder(mPresenter, itemView);
    }

    @Override
    public void onBindViewHolder(MarkerHolder holder, int position) {
        mPresenter.onBindMarkerViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getMarkersCount();
    }

    public class MarkerHolder extends RecyclerView.ViewHolder implements IMarkerItemView {

        private ImageView mImage;
        private TextView mTitle;

        MarkerHolder(final IMapPresenter presenter, View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.rv_item_img);
            mTitle = itemView.findViewById(R.id.rv_item_txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onClickListenerAtPosition(getAdapterPosition());
                }
            });
        }

        @Override
        public void setImg(String[] imgUris) {
            if (imgUris.length > 0)
                Glide.with(mContext)
                        .load(imgUris[0])
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .into(mImage);
        }

        @Override
        public void setTitle(String title) {
            mTitle.setText(title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mTitle.setBackground(
                        ScrimUtil.makeCubicGradientScrimDrawable(
                                Color.BLACK, 2, Gravity.BOTTOM
                        )
                );
            }
        }
    }

}
