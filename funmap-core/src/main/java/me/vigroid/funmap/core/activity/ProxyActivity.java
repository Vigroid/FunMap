package me.vigroid.funmap.core.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by yangv on 1/14/2018.
 * One Activy - many fragments structure App
 * This is the barebone for our main activity(also the only one)
 * We use Fragmentation(by yoke) here to manage fragments
 */

public abstract class ProxyActivity extends AppCompatActivity implements ISupportActivity{

    private final SupportActivityDelegate DELEGATE = new SupportActivityDelegate(this);
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DELEGATE.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }

    /**
     * Override fragmentation methods
     */
    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return DELEGATE;
    }

    @Override
    public ExtraTransaction extraTransaction() {
        return DELEGATE.extraTransaction();
    }

    @Override
    public FragmentAnimator getFragmentAnimator() {
        return DELEGATE.getFragmentAnimator();
    }

    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        DELEGATE.setFragmentAnimator(fragmentAnimator);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return DELEGATE.onCreateFragmentAnimator();
    }

    @Override
    public void onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            this.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(this, "Double click to quit!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DELEGATE.onBackPressed();
    }
}
