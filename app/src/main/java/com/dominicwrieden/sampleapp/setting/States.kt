package com.dominicwrieden.sampleapp.setting

sealed class SettingsState {
    data class SetUp(
        val autoUpdate: Boolean,
        val autoUpdateInterval: Int,
        val minInterval: Int,
        val maxInterval: Int
    ) : SettingsState()

    data class ChangeAutoUpdate(
        val autoUpdate: Boolean,
        val autoUpdateInterval: Int
    ) : SettingsState()

    data class ChangedAutoUpdateInterval(val autoUpdateInterval: Int) : SettingsState()
    object BackgroundChangeSuccessful : SettingsState()
    object BackgroundChangeError : SettingsState()
}