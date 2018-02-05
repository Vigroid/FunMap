package me.vigroid.funmap.impl.view.creart_event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import gapchenko.llttz.Converter;
import gapchenko.llttz.stores.TimeZoneListStore;
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
import me.vigroid.funmap.impl.presenter.create_event.CreateEventPresenterImpl;
import me.vigroid.funmap.impl.presenter.create_event.ICreateEventPresenter;

/**
 * Created by yangv on 2/2/2018.
 * Delegate for event creation, the view layer
 */

public class CreateEventDelegate extends FunMapDelegate implements ICreateEventView {

    private static final String TAG = CreateEventDelegate.class.getSimpleName();
    private static final String ARG_LATLNG = "arg_latLng";
    private final int TYPE_START = 0;
    private final int TYPE_END = 1;
    // Date related
    TimeZone tz;
    Calendar startTime;
    Calendar endTime;
    boolean isPrivate;
    boolean isEveryDay;

    ICreateEventPresenter mPresenter = new CreateEventPresenterImpl(this);
    LatLng latLng = null;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);

    @BindView(R2.id.custom_auto_photo_layout)
    AutoPhotoLayout mAutoPhotoLayout = null;

    @BindView(R2.id.et_title_event)
    TextInputEditText mTitle = null;

    @BindView(R2.id.et_desc_event)
    TextInputEditText mDesc = null;

    @BindView(R2.id.tv_start_time_date)
    TextView mStartDate = null;

    @BindView(R2.id.tv_start_time_hour)
    TextView mStartTime = null;

    @BindView(R2.id.tv_end_time_date)
    TextView mEndDate = null;

    @BindView(R2.id.tv_end_time_hour)
    TextView mEndTime = null;

    @OnItemSelected(R2.id.spinner_ac_event)
    public void spinnerItemSelected(Spinner spinner, int position) {
        isPrivate = (position == 0);
    }

    @BindView(R2.id.rl_start_time)
    RelativeLayout rl_start = null;

    @BindView(R2.id.rl_end_time)
    RelativeLayout rl_end = null;

    @OnCheckedChanged(R2.id.switch_every_day)
    void onSwitchChanged(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            rl_start.setVisibility(View.GONE);
            rl_end.setVisibility(View.GONE);
            isEveryDay = true;
        } else {
            rl_start.setVisibility(View.VISIBLE);
            rl_end.setVisibility(View.VISIBLE);
            isEveryDay = false;
        }
    }

    @OnClick(R2.id.tv_start_time_date)
    void OnClickStartDate() {
        showDatePickDialog(TYPE_START, startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH));
    }

    @OnClick(R2.id.tv_end_time_date)
    void OnClickEndDate() {
        showDatePickDialog(TYPE_END, endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH));
    }

    @OnClick(R2.id.tv_start_time_hour)
    void OnClickStartHour() {
        showTimePickDialog(TYPE_START, startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE));
    }

    @OnClick(R2.id.tv_end_time_hour)
    void OnClickEndHour() {
        showTimePickDialog(TYPE_END, endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE));
    }

    @OnClick(R2.id.btn_create_event_cancel)
    void onClickCancel() {
        pop();
    }

    @OnClick(R2.id.btn_create_event_confirm)
    void onClickConfirm() {
        List<String> list = mAutoPhotoLayout.getImgUris();
        if (checkForm())
            mPresenter.addEventMarker(new MarkerBean((Integer) FunMap.getConfiguration(ConfigKeys.USER_ID), latLng.latitude, latLng.longitude, mTitle.getText().toString(),
                    mDesc.getText().toString(), MarkerType.EVENT_MARKER, false, list.toArray(new String[list.size()]),
                    (String) FunMap.getConfiguration(ConfigKeys.USER_NAME), (String) FunMap.getConfiguration(ConfigKeys.USER_ICON_URI),
                    startTime.getTimeInMillis(), endTime.getTimeInMillis()), isPrivate);
    }

    public static CreateEventDelegate newInstance(LatLng latLng) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_LATLNG, latLng);
        CreateEventDelegate fragment = new CreateEventDelegate();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            latLng = bundle.getParcelable(ARG_LATLNG);
        }
        //get tz by latlng
        tz = Converter.getInstance(TimeZoneListStore.class).getTimeZone(latLng.latitude, latLng.longitude);
        //get converted time
        startTime = Calendar.getInstance(tz);
        endTime = Calendar.getInstance(tz);
        endTime.add(Calendar.HOUR, 1);
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
        mStartDate.setText(dateFormatter.format(startTime.getTime()));
        mStartTime.setText(formatTime(startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE)));
        mEndDate.setText(dateFormatter.format(endTime.getTime()));
        mEndTime.setText(formatTime(endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE)));
        isPrivate = true;
        isEveryDay = false;
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    private boolean checkForm() {
        //check if data input matches our needs. If not return a null and show error.
        boolean isPass = true;
        final String title = mTitle.getText().toString();
        final String desc = mDesc.getText().toString();

        if (title.isEmpty()) {
            mTitle.setError(getString(R.string.title_empty_error));
            isPass = false;
        } else {
            mTitle.setError(null);
        }

        if (title.length() > 20 || desc.length() > 150) isPass = false;

        if (!isEveryDay && startTime.compareTo(endTime) > 0) {
            isPass = false;
            Toast.makeText(_mActivity, R.string.date_error, Toast.LENGTH_SHORT).show();
        }
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
    public void popAndResult(final MarkerBean bean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MapHandler.KEY_RESULT_BEAN, bean);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    private void showDatePickDialog(final int type, int mYear, int mMonth, int mDay) {
        DatePickerDialog dialog = new DatePickerDialog(this.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String dateStr = dateFormatter.format(new GregorianCalendar(i, i1, i2).getTime());
                        if (type == TYPE_START) {
                            startTime.set(Calendar.YEAR, i);
                            startTime.set(Calendar.MONTH, i1);
                            startTime.set(Calendar.DAY_OF_MONTH, i2);
                            mStartDate.setText(dateStr);
                        } else {
                            endTime.set(Calendar.YEAR, i);
                            endTime.set(Calendar.MONTH, i1);
                            endTime.set(Calendar.DAY_OF_MONTH, i2);
                            mEndDate.setText(dateStr);
                        }
                    }
                }, mYear, mMonth, mDay);
        dialog.show();
    }

    private void showTimePickDialog(final int type, int mHour, int mMinute) {
        TimePickerDialog dialog = new TimePickerDialog(this.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (type == TYPE_START) {
                            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            startTime.set(Calendar.MINUTE, minute);
                            mStartTime.setText(formatTime(hourOfDay, minute));
                        } else {
                            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            endTime.set(Calendar.MINUTE, minute);
                            mEndTime.setText(formatTime(hourOfDay, minute));
                        }
                    }
                }, mHour, mMinute, false);
        dialog.show();
    }

    private String formatTime(int hourOfDay, int minute) {
        int hour = hourOfDay % 12;
        return String.format(Locale.US, "%d:%02d %s", hour == 0 ? 12 : hour,
                minute, hourOfDay < 12 ? "AM" : "PM");
    }
}
