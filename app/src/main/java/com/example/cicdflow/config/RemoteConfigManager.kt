package com.example.cicdflow.config

import android.util.Log
import com.example.cicdflow.BuildConfig
import com.example.cicdflow.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class RemoteConfigManager(
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
) {

    fun initialize(onNewHomeEnabledChanged: (Boolean) -> Unit) {
        val minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0L else 3600L
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(minimumFetchIntervalInSeconds)
            .build()

        remoteConfig.setConfigSettingsAsync(settings)
            .addOnCompleteListener {
                remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
                    .addOnCompleteListener {
                        onNewHomeEnabledChanged(isNewHomeEnabled())
                        fetchAndActivate(onNewHomeEnabledChanged)
                    }
            }
    }

    private fun fetchAndActivate(onNewHomeEnabledChanged: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        TAG,
                        "Remote Config updated=${task.result}, $KEY_NEW_HOME_ENABLED=${isNewHomeEnabled()}"
                    )
                    onNewHomeEnabledChanged(isNewHomeEnabled())
                } else {
                    Log.w(TAG, "Remote Config fetch failed", task.exception)
                }
            }
    }

    private fun isNewHomeEnabled(): Boolean {
        return remoteConfig.getBoolean(KEY_NEW_HOME_ENABLED)
    }

    companion object {
        const val KEY_NEW_HOME_ENABLED = "new_home_enabled"
        private const val TAG = "RemoteConfig"
    }
}
