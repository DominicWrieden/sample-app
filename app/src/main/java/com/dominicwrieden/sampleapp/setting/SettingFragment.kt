package com.dominicwrieden.sampleapp.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dominicwrieden.sampleapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingFragment : Fragment() {

    private lateinit var settingsViewModel: SettingViewModel

    private var minAutoUpdateInterval = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUp(true, 150, 100, 200)
    }

    /**
     * Set up setting screen
     */
    private fun setUp(
        autoUpdate: Boolean,
        updateInterval: Int,
        minInterval: Int,
        maxInterval: Int
    ) {
        minAutoUpdateInterval = minInterval
        autoUpdateActivationSwitch.isChecked = autoUpdate
        autoUpdateIntervalSeekBar.isEnabled = autoUpdate
        autoUpdateIntervalSeekBar.max = maxInterval - minInterval
        autoUpdateIntervalSeekBar.progress = updateInterval - minInterval
        autoUpdateInterval.text = if (autoUpdate)
            getString(R.string.setting_auto_update_interval, updateInterval) else ""
    }

    /**
     * Change state if auto update was enabled or disabled
     */
    private fun changeAutoUpdate(state: SettingsState.ChangeAutoUpdate) {
        autoUpdateIntervalSeekBar.isEnabled = state.autoUpdate
        autoUpdateIntervalSeekBar.progress = state.autoUpdateInterval - minAutoUpdateInterval
        autoUpdateInterval.text = if (state.autoUpdate)
            getString(R.string.setting_auto_update_interval, state.autoUpdateInterval) else ""
    }

    /**
     * Change state, if the auto update interval is changed by the seek bar
     */
    private fun changeAutoUpdateInterval(state: SettingsState.ChangedAutoUpdateInterval) {
        autoUpdateInterval.text = getString(
            R.string.setting_auto_update_interval,
            state.autoUpdateInterval
        )
    }


    /**
     * State, which shows, that the background was successfully changed
     */
    private fun backgroundChangeSuccessful() {
        Snackbar.make(
            fragment_setting, R.string.setting_snackbar_background_change_successful,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * State, which shows, that an error occurred while changing the background
     */
    private fun backgroundChangeError() {
        Snackbar.make(
            fragment_setting, R.string.setting_snackbar_background_change_failed,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
