package com.dominicwrieden.sampleapp.data.repository

import com.dominicwrieden.sampleapp.util.SharedPreferenceUtil

class SettingRepository private constructor(
    private val sharedPreferenceUtil: SharedPreferenceUtil
) {
    companion object {
        // For Singleton instantiation
        private var sInstance: SettingRepository? = null

        fun getInstance(sharedPreferenceUtil: SharedPreferenceUtil) =
            sInstance ?: synchronized(this) {
                sInstance ?: SettingRepository(sharedPreferenceUtil).also { sInstance = it }
            }
    }
}