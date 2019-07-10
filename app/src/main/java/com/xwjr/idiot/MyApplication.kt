package com.xwjr.idiot

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import java.util.*
import android.app.PendingIntent



class MyApplication : Application() {


    companion object {
        var mActivityCount = 0
        var open = true
    }

    override fun onCreate() {
        super.onCreate()

        val mTimer = Timer()
        val mTimerTask = object : TimerTask() {
            override fun run() {
                //run方法中执行需要间隔执行的代码
                setTopApp()
            }
        }
        //2s后开始执行，间隔为4s
        mTimer.schedule(mTimerTask, 0, 100)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
                mActivityCount++
                Log.i("idiot", "mActivityCount：$mActivityCount")
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
                mActivityCount--
                Log.i("idiot", "mActivityCount：$mActivityCount")
                if (mActivityCount == 0) {
                    val intent = Intent(this@MyApplication, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val pendingIntent = PendingIntent.getActivity(this@MyApplication, 0, intent, 0)
                    try {
                        pendingIntent.send()
                    } catch (e: PendingIntent.CanceledException) {
                        e.printStackTrace()
                    }
                    if (isExistActivity(MainActivity())) {
                        setTopApp()
                    } else {
                        val intent = Intent(this@MyApplication, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }

        })
    }
}