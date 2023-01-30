package com.example.todoapp.data.models

data class RecyclerDataModel(
    var id:Int,
    var title:String,
    var priority: Priority,
    var description:String,
    var date:String
)
