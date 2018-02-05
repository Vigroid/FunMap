package me.vigroid.funmap.impl.view.create_pic;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import me.vigroid.funmap.core.app.ConfigKeys;
import me.vigroid.funmap.core.app.FunMap;
import me.vigroid.funmap.core.bean.MarkerBean;
import me.vigroid.funmap.core.bean.MarkerType;
import me.vigroid.funmap.core.fragments.FunMapDelegate;
import me.vigroid.funmap.core.ui.loader.FunMapLoader;
import me.vigroid.funmap.core.ui.loader.LoaderStyle;
import me.vigroid.funmap.core.ui.widget.AutoPhotoLayout;
import me.vigroid.funmap.core.utils.callback.CallbackManager;
import me.vigroid.funmap.core.utils.callback.CallbackType;
import me.vigroid.funmap.core.utils.callback.IGlobalCallback;
import me.vigroid.funmap.impl.R;
import me.vigroid.funmap.impl.R2;
import me.vigroid.funmap.impl.lbs.MapHandler;
import me.vigroid.funmap.impl.presenter.create_pic.CreatePicPresenterImpl;
import me.vigroid.funmap.impl.presenter.create_pic.ICreatePicPresenter;

/**
 * Created by yangv on 2/4/2018.
 * V level
 */

public class CreatePicDelegate extends FunMapDelegate implements ICreatePicView{

    private static final String ARG_LATLNG = "arg_latLng";
    private LatLng latLng;
    private ICreatePicPresenter mPresenter = new CreatePicPresenterImpl(this);
    boolean isPrivate;

    @BindView(R2.id.custom_auto_photo_layout_create_pic)
    AutoPhotoLayout mAutoPhotoLayout = null;

    @BindView(R2.id.et_title_pic)
    TextInputEditText mTitle = null;

    @OnClick(R2.id.btn_create_pic_cancel)
    void onClickCancel(){pop();}

    @OnClick(R2.id.btn_create_pic_confirm)
    void onClickConfirm(){
        List<String> list = mAutoPhotoLayout.getImgUris();
        if (checkForm())
            mPresenter.addPicMarker(new MarkerBean((Integer) FunMap.getConfiguration(ConfigKeys.USER_ID), latLng.latitude, latLng.longitude, mTitle.getText().toString(),
                    "", MarkerType.PICS_MARKER, false, list.toArray(new String[list.size()]), (String) FunMap.getConfiguration(ConfigKeys.USER_NAME),
                    (String) FunMap.getConfiguration(ConfigKeys.USER_ICON_URI), -100, -100), isPrivate);
    }

    @OnItemSelected(R2.id.spinner_ac_pic)
    public void spinnerItemSelected(Spinner spinner, int position) {
        isPrivate = (position == 0);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_create_pic_marker;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            latLng = bundle.getParcelable(ARG_LATLNG);
        }
    }

    public static CreatePicDelegate newInstance(LatLng latLng) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_LATLNG, latLng);

        CreatePicDelegate fragment = new CreatePicDelegate();
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    private boolean checkForm(){
        boolean isPass = true;
        final String title = mTitle.getText().toString();

        if (title.isEmpty()) {
            mTitle.setError(getString(R.string.title_empty_error));
            isPass = false;
        } else {
            mTitle.setError(null);
        }

        if (mAutoPhotoLayout.getImgUris().size()<=0){
            isPass = false;
            Toast.makeText(_mActivity, R.string.img_empty_error, Toast.LENGTH_SHORT).show();
        }

        if (title.length() > 20) isPass = false;
        return isPass;
    }

    @Override
    public void showLoader() {
        FunMapLoader.showLoading(_mActivity, LoaderStyle.BallSpinFadeLoaderIndicator.name());
    }

    @Override
    public void stopLoader() {
        FunMapLoader.stopLoading();
    }

    @Override
    public void showError() {
        Toast.makeText(_mActivity, R.string.upload_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void popAndResult(MarkerBean bean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MapHandler.KEY_RESULT_BEAN, bean);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }
}
