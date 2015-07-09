package com.example.yamaguchi.arduinotest.common;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import rx.android.lifecycle.LifecycleEvent;
import rx.android.lifecycle.LifecycleObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

/**
 *
 */
public abstract class BaseActivity extends ActionBarActivity {

    private final BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();

    public Observable<LifecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    public <T> Observable<T> bind(Observable<T> observable) {
        return LifecycleObservable.bindActivityLifecycle(
                lifecycle(),
                observable.observeOn(AndroidSchedulers.mainThread()));
    }

    @Getter
    protected Toolbar toolBar;

    @Setter @Getter
    protected View toolBarView;

    @Getter
    protected LinearLayout baseContentLl;

//    private DrawerLayout mBaseView;

    private boolean mIsHomeAsUpEnabled = false;

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(LifecycleEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(LifecycleEvent.PAUSE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(LifecycleEvent.START);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycleSubject.onNext(LifecycleEvent.STOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(LifecycleEvent.CREATE);

//        mBaseView = (DrawerLayout) getLayoutInflater().inflate(R.layout.base_activity, null);
//        setContentView(mBaseView);

//        setSupportActionBar(toolBar);
//        setActionBar();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY);
        super.onDestroy();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // CutomViewを表示するか
        actionBar.setDisplayShowCustomEnabled(false);

        // iconを表示するか
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setHomeButtonEnabled(true);

        // 戻るボタンを表示するかどうか
        actionBar.setDisplayHomeAsUpEnabled(mIsHomeAsUpEnabled);

        // タイトルを表示するか
        actionBar.setDisplayShowTitleEnabled(false);
    }
}

