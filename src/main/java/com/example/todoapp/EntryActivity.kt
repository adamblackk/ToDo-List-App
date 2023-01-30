package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.todoapp.databinding.ActivityEntryBinding
import com.example.todoapp.util.SaveData
import com.example.todoapp.viewmodel.SharedViewModel

class EntryActivity : AppCompatActivity(), View.OnClickListener {

    private val sharedViewModel:SharedViewModel by viewModels()

    private lateinit var myShare:SharedPreferences
    private lateinit var binding: ActivityEntryBinding
    private lateinit var saveData: SaveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myShare=sharedViewModel.sharedPreferences(application)
        val selectedTheme=myShare.getInt("theme",0)

        saveData= SaveData(this)
        if (saveData.loadDarkModeState()){
            setTheme(R.style.DarkTheme_ToDoApp)
        }else {
            setDynamicTheme(selectedTheme)
        }
        binding = ActivityEntryBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.btnNext.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }

        if (saveData.loadDarkModeState()){
            saveData.setDarkModeState(false)
           setOnclick()
        }else{
            setOnclick()
        }

    }
    private fun setOnclick(){
        binding.btnTheme.setOnClickListener(this)
        binding.btnTheme1.setOnClickListener(this)
        binding.btnTheme2.setOnClickListener(this)
        binding.btnTheme3.setOnClickListener(this)
        binding.btnTheme4.setOnClickListener(this)
    }

    private fun setDynamicTheme(selectedTheme: Int) {
        when(selectedTheme){
            1 ->setTheme(R.style.Theme_ToDoApp)
            2 ->setTheme(R.style.DynamicTheme1)
            3 ->setTheme(R.style.DynamicTheme2)
            4 ->setTheme(R.style.DynamicTheme3)
            5 ->setTheme(R.style.DynamicTheme4)
        }
    }

    override fun onClick(v: View?) {
        val editor=myShare.edit()
        when(v?.id){
            R.id.btn_theme -> {editor.putInt("theme",1)}
            R.id.btn_theme1 -> {editor.putInt("theme",2)}
            R.id.btn_theme2 -> {editor.putInt("theme",3)}
            R.id.btn_theme3 -> {editor.putInt("theme",4)}
            R.id.btn_theme4 -> {editor.putInt("theme",5)}
        }
        editor.apply()
        val intent=Intent(this,EntryActivity::class.java)
        startActivity(intent)
        finish()


    }






}