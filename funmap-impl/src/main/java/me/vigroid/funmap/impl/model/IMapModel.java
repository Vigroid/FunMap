package me.vigroid.funmap.impl.model;

import java.util.List;

import me.vigroid.funmap.impl.bean.MarkerBean;

/**
 * Created by yangv on 1/30/2018.
 */

public interface IMapModel {

    List<MarkerBean> fetchMarkers();
}
