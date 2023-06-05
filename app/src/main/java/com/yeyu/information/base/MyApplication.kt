package com.yeyu.information.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        private lateinit var app: MyApplication
        fun getApp() = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        setLifeCallBack()
    }

    //Activity集合，来管理所有的Activity
    private val activities: MutableList<Activity> = mutableListOf()

    private fun setLifeCallBack() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activities.isNotEmpty()) {
                    activities.remove(activity)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                activities.add(activity)
            }

            override fun onActivityResumed(activity: Activity) {
            }

        })
    }

    fun clearActivities() {
        activities.forEach {
            it.finish()
        }
    }

}