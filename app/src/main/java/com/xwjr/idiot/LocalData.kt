package com.xwjr.idiot

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("ApplySharedPref")
object LocalData {
    fun saveData(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("localData", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).commit()
    }

    fun getData(context: Context, key: String): String {
        val sharedPreferences = context.getSharedPreferences("localData", Context.MODE_PRIVATE)
        var data = sharedPreferences.getString(key, "")
        if (data == null) data = ""
        return data
    }
}
