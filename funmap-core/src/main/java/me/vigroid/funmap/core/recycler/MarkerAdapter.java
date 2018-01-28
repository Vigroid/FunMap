package me.vigroid.funmap.core.recycler;

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

import java.util.ArrayList;
import java.util.List;

import me.vigroid.funmap.core.R;
import me.vigroid.funmap.core.utils.ui.ScrimUtil;

/**
 * Created by yangv on 1/25/2018.
 * Adapter for the rv
 */

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.MarkerHolder>{

    public class MarkerHolder extends RecyclerView.ViewHolder{

        private ImageView mImage;
        private TextView mTitle;

        public MarkerHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.rv_item_img);
            mTitle = itemView.findViewById(R.id.rv_item_txt);
        }
    }

    private List<MarkerBean> mBeans;
    private List<MarkerBean> mBeansFiltered;
    private Context mContext;

    public MarkerAdapter(List beans, Context context) {
        this.mBeans = beans;
        this.mBeansFiltered = beans;
        this.mContext = context;
    }

    @Override
    public MarkerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_marker_item, parent, false);
        MarkerHolder holder = new MarkerHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MarkerHolder holder, int position) {
        final MarkerBean bean = mBeansFiltered.get(position);
        Glide.with(mContext)
                .load(bean.imgUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .centerCrop()
                .into(holder.mImage);
        holder.mTitle.setText(bean.title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.mTitle.setBackground(
                    ScrimUtil.makeCubicGradientScrimDrawable(
                            Color.BLACK, 2, Gravity.BOTTOM
                    )
            );
        }
    }

    @Override
    public int getItemCount() {
        return mBeansFiltered.size();
    }

    public void filterSearch(CharSequence charSequence){
        String filterString = charSequence.toString();
        if (filterString.isEmpty()){
           mBeansFiltered = mBeans;
        } else {
            List<MarkerBean> tempBeans = new ArrayList<>();
            for(MarkerBean bean: mBeans){
                if(bean.title.toLowerCase().contains(filterString.toLowerCase())){
                    tempBeans.add(bean);
                }
            }
            mBeansFiltered = tempBeans;
        }
        notifyDataSetChanged();
    }

}
