package com.example.todoapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.todoapp.util.SaveData
import com.example.todoapp.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var myShare: SharedPreferences
    private val sharedViewModel:SharedViewModel by viewModels()
    private lateinit var saveData: SaveData
    private lateinit var navController: NavController
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
        setContentView(R.layout.activity_main)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.navController
        setupActionBarWithNavController(navController)

    }

        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp() || super.onSupportNavigateUp()
        }
    private fun setDynamicTheme(selectedTheme: Int) {
        when (selectedTheme) {
            1 -> setTheme(R.style.Theme_ToDoApp)
            2 -> setTheme(R.style.DynamicTheme1)
            3 -> setTheme(R.style.DynamicTheme2)
            4 -> setTheme(R.style.DynamicTheme3)
            5 -> setTheme(R.style.DynamicTheme4)
        }
    }

}