package me.vigroid.funmap.impl.model;

import java.util.ArrayList;
import java.util.List;

import me.vigroid.funmap.impl.bean.MarkerBean;
import me.vigroid.funmap.impl.bean.MarkerType;

/**
 * Created by yangv on 1/30/2018.
 */

public class MapModelImpl implements IMapModel{

    @Override
    public List<MarkerBean> fetchMarkers(){
        MarkerBean bean1 = new MarkerBean(new String[]{"https://cnet4.cbsistatic.com/img/I-2dhG3a_A2B-LPW1vtnnnynxjk=/830x467/2017/10/16/53715bdb-d189-4aae-8e8b-f48cbe5d9512/google-pixel-2-0304-013.jpg"}, "Title A", "", 32.986645, -96.795427, MarkerType.PICS_MARKER, 1, false, false);
        MarkerBean bean2 = new MarkerBean(new String[]{"https://cdn-images-1.medium.com/max/1600/1*LElUGGMInnIAl4QpKwuP1Q.png", "http://blogs-images.forbes.com/gordonkelly/files/2017/06/Screenshot-2017-06-11-at-21.36.02.png"}, "Title B", "heihei", 32.996645, -96.785427, MarkerType.EVENT_MARKER, 2, false, false
        );
        MarkerBean bean3 = new MarkerBean(new String[]{"http://blogs-images.forbes.com/gordonkelly/files/2017/06/Screenshot-2017-06-11-at-21.36.02.png"}, "Title C", "xiexie", 32.999645, -96.799427, MarkerType.EVENT_MARKER, 3, false, false);
        MarkerBean bean4 = new MarkerBean(new String[]{"http://blogs-images.forbes.com/gordonkelly/files/2017/06/Screenshot-2017-06-11-at-21.36.02.png"}, "Title D", "cong ming", 32.987645, -96.790427, MarkerType.PICS_MARKER, 4, false, false);

        List<MarkerBean> beans = new ArrayList<>();

        beans.add(bean1);
        beans.add(bean2);
        beans.add(bean3);
        beans.add(bean4);

        return beans;
    }
}
