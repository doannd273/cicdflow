package com.example.cicdflow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cicdflow.config.RemoteConfigManager
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private val remoteConfigManager = RemoteConfigManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseCrashlytics.getInstance().apply {
            setCustomKey("version_name", "JFDJA")
            setCustomKey("version_code", "FADFASD")
            setUserId("123")
        }

        remoteConfigManager.initialize { newHomeEnabled ->
            Log.d(TAG, "new_home_enabled=$newHomeEnabled")
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.tasks.observe(this) {
            // update UI
        }

        viewModel.loadTasks()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
