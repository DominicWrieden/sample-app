package com.dominicwrieden.sampleapp.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeWith(owner: LifecycleOwner, observer: (T) -> Unit) =
    observe(owner.getViewLifecycleOwnerIfFragment(), Observer { observer(it!!) })

fun LifecycleOwner.getViewLifecycleOwnerIfFragment() =
    if (this is Fragment) this.viewLifecycleOwner else this