package com.example.todoapp.util


import android.content.Context
import android.content.SharedPreferences



class SaveData (context: Context){
   private var mySharePref:SharedPreferences=context.getSharedPreferences("file",Context.MODE_PRIVATE)


    fun setDarkModeState(state:Boolean?){
        val editor:SharedPreferences.Editor=mySharePref.edit()
        editor.putBoolean("Dark",state!!)
        editor.apply()
    }

    fun loadDarkModeState(): Boolean {
        return mySharePref.getBoolean("Dark", false)
    }


}