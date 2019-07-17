package com.xwjr.idiot

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {

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
    }

    //默认值数据
    var selectHour = "00"
    var selectMinute = "00"
    var selectSecond = "05"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //设置页面样式
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)


        //初始化滑轮选择器
        hours.offset = 0
        hours.setItems(selectAbleHour)
        hours.setSeletion(selectHour.toInt())
        hours.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String) {
                Log.i("idiot", "选择的position：$selectedIndex")
                selectHour = item
            }
        })

        minute.offset = 0
        minute.setItems(selectAbleMinute)
        minute.setSeletion(selectMinute.toInt())
        minute.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String) {
                Log.i("idiot", "选择的position：$selectedIndex")
                selectMinute = item
            }
        })

        second.offset = 0
        second.setItems(selectAbleSecond)
        second.setSeletion(selectSecond.toInt())
        second.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String) {
                Log.i("idiot", "选择的position：$selectedIndex")
                selectSecond = item
            }
        })


        //开始倒计时点击事件
        iv_start.setOnClickListener {
            LocalData.saveData(this, "isOpen", "true")
            LocalData.saveData(this, "countDownTime", (selectHour.toLong() * 60 * 60 * 1000 + selectMinute.toLong() * 60 * 1000 + selectSecond.toLong() * 1000).toString())
            startCountDown()
        }
    }

    override fun onResume() {
        super.onResume()
        startCountDown()
    }

    //开始倒计时
    private fun startCountDown() {
        if (LocalData.getData(this, "isOpen") == "true") {
            tv_time.visibility = View.VISIBLE
            tv_time_millisecond.visibility = View.VISIBLE
            ll_wheel.visibility = View.GONE
            iv_start.visibility = View.GONE
            val countDownTimer = object : CountDownTimer(LocalData.getData(this, "countDownTime").toLong(), 100) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    LocalData.saveData(this@MainActivity, "countDownTime", millisUntilFinished.toString())
                    Log.i("idiot", "倒计时：$millisUntilFinished")
                    tv_time.text = formatLongToTimeStr(millisUntilFinished)
                    tv_time_millisecond.text = "." + formatLongToMillisecondStr(millisUntilFinished)
                }

                override fun onFinish() {
                    LocalData.saveData(this@MainActivity, "isOpen", "false")
                    tv_time.visibility = View.GONE
                    tv_time_millisecond.visibility = View.GONE
                    ll_wheel.visibility = View.VISIBLE
                    iv_start.visibility = View.VISIBLE
                }
            }
            countDownTimer.start()
        }
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
    private fun format2digit(data: Int): String {
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

    //特定条件屏蔽触摸操作
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (LocalData.getData(this, "isOpen") == "true") {
            return true
        }
        return super.onTouchEvent(event)
    }

    //特定条件屏蔽按键操作
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.i("idiot", "按下键值：$keyCode")
        if (LocalData.getData(this, "isOpen") == "true") {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
