package com.xwjr.idiot

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import com.contrarywind.adapter.WheelAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)


//        tv_close.setOnClickListener {
//            MyApplication.open = false
//        }


        hours.offset = 1
        hours.setItems(arrayListOf("11","33","32","342","32424","23423432"))
        hours.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                Log.i("idiot", "选择的position：$selectedIndex")
            }
        })

        minute.offset = 1
        minute.setItems(arrayListOf("11","33","32","342","32424","23423432"))
        minute.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                Log.i("idiot", "选择的position：$selectedIndex")
            }
        })

        second.offset = 1
        second.setItems(arrayListOf("11","33","32","342","32424","23423432"))
        second.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                Log.i("idiot", "选择的position：$selectedIndex")
            }
        })


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.i("idiot", "按下键值：$keyCode")
        return true
    }
}
