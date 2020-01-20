package com.dominicwrieden.sampleapp.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

fun <T> Observable<T>.toLiveData() =
    LiveDataReactiveStreams.fromPublisher(toFlowable(BackpressureStrategy.LATEST))

fun <T> Flowable<T>.toLiveData() =
    LiveDataReactiveStreams.fromPublisher(this)

fun <T> LiveData<T>.observeWith(owner: LifecycleOwner, observer: (T) -> Unit) =
    observe(owner.getViewLifecycleOwnerIfFragment(), Observer { observer(it!!) })

fun LifecycleOwner.getViewLifecycleOwnerIfFragment() =
    if (this is Fragment) this.viewLifecycleOwner else this


fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }