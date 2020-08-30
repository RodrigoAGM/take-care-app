package com.example.takecare

import android.app.Application
import com.example.takecare.utils.PreferenceHelper

class TakeCareApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.init(this)
    }
}