package com.sexyslave.coursesapp

import android.app.Application
import com.sexyslave.coursesapp.di.appModule
import com.sexyslave.coursesapp.di.dataModule
import com.sexyslave.coursesapp.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule, domainModule, dataModule)
        }
    }
}