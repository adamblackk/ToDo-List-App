package com.example.todoapp.data

import androidx.room.TypeConverter
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.RecyclerData
import com.example.todoapp.data.models.RecyclerDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    val gson= Gson()
    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }
    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

    @TypeConverter
    fun foodRecipeToString(recyclerDataModel: RecyclerDataModel):String{
        return gson.toJson(recyclerDataModel)
    }

    @TypeConverter
    fun stringToRecipe(data:String): RecyclerDataModel {
        val listType=object : TypeToken<RecyclerDataModel>(){}.type
        return gson.fromJson(data,listType)
    }
}