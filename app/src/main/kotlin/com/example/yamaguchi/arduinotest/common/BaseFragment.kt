package com.example.yamaguchi.arduinotest.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

import rx.Observable
import rx.android.lifecycle.LifecycleEvent
import rx.android.lifecycle.LifecycleObservable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject

/**

 */
public class BaseFragment : Fragment() {

    private val lifecycleSubject = BehaviorSubject.create<LifecycleEvent>()

    public fun lifecycle(): Observable<LifecycleEvent> {
        return lifecycleSubject.asObservable()
    }

    /**
     * [rx.android.lifecycle.LifecycleObservable.bindFragmentLifecycle] を行うヘルパメソッド
     * 戻り値のObservable を subscribe した場合、適切に unsubscribeされる
     */
    protected fun <T> bind(observable: Observable<T>): Observable<T> {
        return LifecycleObservable.bindFragmentLifecycle(lifecycle(), observable.observeOn(AndroidSchedulers.mainThread()))
    }


    override fun onAttach(activity: android.app.Activity?) {
        super.onAttach(activity)
        lifecycleSubject.onNext(LifecycleEvent.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(LifecycleEvent.CREATE)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleSubject.onNext(LifecycleEvent.CREATE_VIEW)
    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(LifecycleEvent.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(LifecycleEvent.RESUME)
    }

    override fun onPause() {
        lifecycleSubject.onNext(LifecycleEvent.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        lifecycleSubject.onNext(LifecycleEvent.STOP)
        super.onStop()
    }

    override fun onDestroyView() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY_VIEW)
        super.onDestroyView()
    }

    override fun onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY)
        super.onDestroy()
    }

    override fun onDetach() {
        lifecycleSubject.onNext(LifecycleEvent.DETACH)
        super.onDetach()
    }
}