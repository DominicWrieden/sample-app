package com.dominicwrieden.sampleapp.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(
    private val context: Context
) {

    companion object {
        const val SHARED_PREFERENCES = "SHARED_PREFERENCES_SAMPLE_APP"
    }

    val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(
            SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

    fun retrieveFromSharedPreferences(key: String): String =
        sharedPreferences.getString(key, "") ?: ""

    fun saveToSharedPreferences(key: String, value: String) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }
}