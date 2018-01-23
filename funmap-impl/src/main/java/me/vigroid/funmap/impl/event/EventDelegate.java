package me.vigroid.funmap.impl.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.impl.R;

/**
 * Created by yangv on 1/20/2018.
 */

public class EventDelegate extends FunMapDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_event;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

    }
}
