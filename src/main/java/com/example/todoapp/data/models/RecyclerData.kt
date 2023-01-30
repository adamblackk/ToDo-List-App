package com.example.todoapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recyclerData")
data class RecyclerData (
  val recyclerDataModel: RecyclerDataModel
        ){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
