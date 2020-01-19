package com.dominicwrieden.sampleapp

import com.dominicwrieden.sampleapp.data.local.room.AppDatabase
import com.dominicwrieden.sampleapp.data.remote.Api
import com.dominicwrieden.sampleapp.data.repository.PersonRepository
import com.dominicwrieden.sampleapp.data.repository.PostRepository
import com.dominicwrieden.sampleapp.data.repository.SettingRepository
import com.dominicwrieden.sampleapp.util.SharedPreferenceUtil
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
object Dependencies {

    private val moshi = Moshi.Builder().build()
    private val httpClient = OkHttpClient.Builder().build()
    private val api = Api(httpClient, moshi)
    private val roomDb by lazy {AppDatabase.getInstance(App.instance)}
    private val sharedPreferenceUtil by lazy { SharedPreferenceUtil(App.instance) }
    private val personRepository by lazy {PersonRepository.getInstance(roomDb.getPeronDao())}
    private val postRepository by lazy { PostRepository.getInstance(roomDb.getPostDao(), api) }
    private val settingRepository by lazy { SettingRepository.getInstance(sharedPreferenceUtil) }


    fun <T> inject(clazz: Class<T>): T {
        return when (clazz) {
            Moshi::class.java -> moshi
            AppDatabase::class.java -> roomDb
            PersonRepository::class.java -> personRepository
            PostRepository::class.java -> postRepository
            SharedPreferenceUtil::class.java -> settingRepository
            else -> throw IllegalStateException("Can't find dependency for ${clazz.name}")
        } as T
    }


    inline operator fun <reified T> getValue(headlinesViewModel: Any, property: KProperty<*>): T {
        return inject(T::class.java)
    }
}