package com.xwjr.idiot

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log

//初始化可选择数据
val selectAbleHour = arrayListOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
        "20", "21", "22", "23"
)
val selectAbleMinute = arrayListOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
        "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
        "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
        "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
)
val selectAbleSecond = arrayListOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
        "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
        "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
        "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
)

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

fun WheelView.init(selectAble: ArrayList<String>, selected: String, deal: ((value: String) -> Unit)? = null) {
    this.offset = 0
    this.setItems(selectAble)
    this.setSeletion(selected.toInt())
    this.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
        override fun onSelected(selectedIndex: Int, item: String) {
            Log.i("idiot", "选择的position：$selectedIndex")
            if (deal != null) {
                deal(item)
            }
        }
    })
}

//是否开启了倒计时
fun Context.isOpenCountDown(): Boolean {
    return LocalData.getData(this, "isOpen") == "true"
}

//毫秒转 时分秒显示
fun formatLongToTimeStr(l: Long): String {
    var hour = 0
    var minute = 0
    var second = 0
    var milliSecond: Int = l.toInt()
    if (milliSecond >= 1000) {
        second = milliSecond / 1000
        milliSecond %= 1000
    }
    if (second >= 60) {
        minute = second / 60
        second %= 60
    }
    if (minute > 60) {
        hour = minute / 60
        minute %= 60
    }
    return when {
        hour > 0 -> "${format2digit(hour)}:${format2digit(minute)}:${format2digit(second)}"
        minute > 0 -> "${format2digit(minute)}:${format2digit(second)}"
        else -> format2digit(second)
    }
}

//毫秒转 时分秒显示
fun formatLongToMillisecondStr(l: Long): String {
    var mSecond: Int = l.toInt()
    if (mSecond >= 1000) {
        mSecond %= 1000
    }
    return format3digit(mSecond)
}

//时分秒格式化显示
fun format2digit(data: Int): String {
    return when {
        data.toString().length == 1 -> "0$data"
        else -> "$data"
    }
}

//毫秒格式化显示
private fun format3digit(data: Int): String {
    return when {
        data.toString().length == 1 -> "00$data"
        data.toString().length == 2 -> "0$data"
        else -> "$data"
    }
}
