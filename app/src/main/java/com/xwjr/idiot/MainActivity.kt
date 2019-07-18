package com.xwjr.idiot

import android.annotation.SuppressLint
import android.app.Instrumentation
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

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
        hours.init(selectAbleHour,selectHour){
            selectHour = it
        }
        minute.init(selectAbleMinute,selectMinute){
            selectMinute = it
        }
        second.init(selectAbleSecond,selectSecond){
            selectSecond = it
        }

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
        if (isOpenCountDown()) {
            tv_time.visibility = View.VISIBLE
            tv_time_millisecond.visibility = View.VISIBLE
            ll_wheel.visibility = View.GONE
            iv_start.visibility = View.GONE
            val countDownTimer = object : CountDownTimer(LocalData.getData(this, "countDownTime").toLong(), 100) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    LocalData.saveData(this@MainActivity, "countDownTime", millisUntilFinished.toString())
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


    //特定条件屏蔽触摸操作
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isOpenCountDown()) {
            return true
        }
        return super.onTouchEvent(event)
    }

    //特定条件屏蔽按键操作
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.i("idiot", "按下键值：$keyCode")
        if (isOpenCountDown()) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
