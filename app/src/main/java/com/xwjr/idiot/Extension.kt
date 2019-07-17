package com.xwjr.idiot

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * 判断指定Activity是否存在
 */
fun Context.isExistActivity(activity: Activity): Boolean {
    try {
        val intent = Intent(this, activity::class.java)
        val cmpName = intent.resolveActivity(packageManager)
        var flag = false
        if (cmpName != null) { // 说明系统中存在这个activity
            val am = getSystemService(Context.ACTIVITY_SERVICE)
            am as ActivityManager
            val taskInfoList = am.getRunningTasks(100)
            for (item in taskInfoList) {
                if (item.baseActivity == cmpName) {
                    flag = true
                    break
                }
            }
        }
        return flag
    } catch (e: Exception) {
        Log.e("idiot", "判断指定Activity是否存在时发生异常")
        e.printStackTrace()
        return false
    }
}


/**
 * 将应用进程置顶
 */
fun Context.setTopApp() {
    try {
        if (LocalData.getData(this, "isOpen") != "true") {
            return
        }
        if (MyApplication.mActivityCount == 1) {
            return
        } else {
            Log.e("idiot", "app已切换到后台，需要置顶操作")
        }
        //    if (!isRunningForeground()) {
        val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE)
        activityManager as ActivityManager
        val taskInfoList = activityManager.getRunningTasks(100)
        for (item in taskInfoList) {
            /**找到本应用的 task，并将它切换到前台*/
            if (item.topActivity.packageName == packageName) {
                activityManager.moveTaskToFront(item.id, 0)
                break
            }
        }
//    }
    } catch (e: Exception) {
        Log.e("idiot", "将应用进程置顶时发生异常")
        e.printStackTrace()
    }
}
