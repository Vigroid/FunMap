package me.vigroid.funmap.impl.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.core.ui.widget.AutoPhotoLayout;
import me.vigroid.funmap.core.utils.callback.CallbackManager;
import me.vigroid.funmap.core.utils.callback.CallbackType;
import me.vigroid.funmap.core.utils.callback.IGlobalCallback;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.R2;
import me.vigroid.funmap.impl.lbs.MapHandler;

/**
 * Created by yangv on 2/2/2018.
 * Delegate for event creation, the view layer
 */

public class CreateEventDelegate extends FunMapDelegate{

    @BindView(R2.id.custom_auto_photo_layout)
    AutoPhotoLayout mAutoPhotoLayout = null;

    @OnClick(R2.id.btn_create_event_confirm)
    void onClickConfirm(){
        Bundle bundle = new Bundle();
        bundle.putString(MapHandler.KEY_RESULT_BEAN,"heihei");
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_create_event_marker;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        mAutoPhotoLayout.setDelegate(this);
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(Uri args) {
                        mAutoPhotoLayout.onCropTarget(args);
                    }
                });
    }
}
