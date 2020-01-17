package com.dominicwrieden.sampleapp

import com.squareup.moshi.Moshi
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
object Dependencies {

    private val moshi = Moshi.Builder()
        .build()

    fun <T> inject(clazz: Class<T>): T {
        return when (clazz) {
            Moshi::class.java -> moshi
            else -> throw IllegalStateException("Can't find dependency for ${clazz.name}")
        } as T
    }


    inline operator fun <reified T> getValue(headlinesViewModel: Any, property: KProperty<*>): T {
        return inject(T::class.java)
    }
}