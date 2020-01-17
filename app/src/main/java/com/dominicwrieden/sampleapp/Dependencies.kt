package com.dominicwrieden.sampleapp

import com.dominicwrieden.sampleapp.data.local.room.AppDatabase
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.squareup.moshi.Moshi
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
object Dependencies {

    private val moshi = Moshi.Builder()
        .build()
    private val roomDb by lazy {AppDatabase.getInstance(App.instance)}
    private val personRepository by lazy {PersonRepository.getInstance(roomDb.getPeronDao())}


    fun <T> inject(clazz: Class<T>): T {
        return when (clazz) {
            Moshi::class.java -> moshi
            AppDatabase::class.java -> roomDb
            PersonRepository::class.java -> personRepository
            else -> throw IllegalStateException("Can't find dependency for ${clazz.name}")
        } as T
    }


    inline operator fun <reified T> getValue(headlinesViewModel: Any, property: KProperty<*>): T {
        return inject(T::class.java)
    }
}