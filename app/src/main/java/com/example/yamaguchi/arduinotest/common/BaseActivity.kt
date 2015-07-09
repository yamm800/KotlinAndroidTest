package com.example.yamaguchi.arduinotest.common

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.LinearLayout

import lombok.Getter
import lombok.Setter
import rx.Observable
import rx.android.lifecycle.LifecycleEvent
import rx.android.lifecycle.LifecycleObservable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject

/**

 */
public abstract class BaseActivity : ActionBarActivity() {

    private val lifecycleSubject = BehaviorSubject.create<LifecycleEvent>()

    public fun lifecycle(): Observable<LifecycleEvent> {
        return lifecycleSubject.asObservable()
    }

    public fun <T> bind(observable: Observable<T>): Observable<T> {
        return LifecycleObservable.bindActivityLifecycle(lifecycle(), observable.observeOn(AndroidSchedulers.mainThread()))
    }

    private val mIsHomeAsUpEnabled = false

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(LifecycleEvent.RESUME)
    }

    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(LifecycleEvent.PAUSE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(LifecycleEvent.START)
    }

    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(LifecycleEvent.STOP)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(LifecycleEvent.CREATE)
    }

    override fun onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY)
        super.onDestroy()
    }

    private fun setActionBar() {
        val actionBar = getSupportActionBar()
        // CutomViewを表示するか
        actionBar.setDisplayShowCustomEnabled(false)

        // iconを表示するか
        actionBar.setDisplayShowHomeEnabled(true)

        actionBar.setHomeButtonEnabled(true)

        // 戻るボタンを表示するかどうか
        actionBar.setDisplayHomeAsUpEnabled(mIsHomeAsUpEnabled)

        // タイトルを表示するか
        actionBar.setDisplayShowTitleEnabled(false)
    }
}

