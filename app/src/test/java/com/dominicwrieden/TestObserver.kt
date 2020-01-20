package com.dominicwrieden

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * Soure: Nicolas Duponchel at Medium
 * https://medium.com/@nicolas.duponchel/testing-viewmodel-in-mvvm-using-livedata-and-rxjava-b27878495220
 */
class TestObserver<T> : Observer<T> {

    val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        observedValues.add(value)
    }
}


fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}