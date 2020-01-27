package com.cxwl.ichangxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.cxwl.ichangxing.R;
import com.cxwl.ichangxing.utils.SPreferenceUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SpalshActivity extends FragmentActivity {
    private static final long SPLASH_DELAY_SECONDS = 3;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        init();

    }

    private void init() {
        doInterval();
    }
    private void doInterval() {
        Disposable mIntervalDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(SPLASH_DELAY_SECONDS + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return SPLASH_DELAY_SECONDS - aLong;
                    }
                }).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (aLong == 0) {
                            goActivity();
                        }
                    }
                });
        mCompositeDisposable.add(mIntervalDisposable);
    }

    private void goActivity() {
        String userid = SPreferenceUtil.getInstance(this).getUserId();
        if (TextUtils.isEmpty(userid)) {
            goLogin();
        } else {
            goHome();
        }
    }

    private void goLogin() {
        startActivity(new Intent(SpalshActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    private void goHome() {
        startActivity(new Intent(SpalshActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
