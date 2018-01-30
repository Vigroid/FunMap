package me.vigroid.funmap;

import android.os.Bundle;

import butterknife.ButterKnife;
import me.vigroid.funmap.core.activity.ProxyActivity;
import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.impl.view.MapDelegateImpl;

public class MapsActivity extends ProxyActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);
        FunMap.getConfigurator().withActivity(this);

        if (savedInstanceState == null) {
            //load the root fragment into our container
            loadRootFragment(R.id.root_delegate_container, new MapDelegateImpl());
        }
    }
}
