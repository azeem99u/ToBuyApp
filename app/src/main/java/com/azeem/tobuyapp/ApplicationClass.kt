package com.azeem.tobuyapp

import android.app.Application

class ApplicationClass:Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPrefUtil.init(this)
    }

}