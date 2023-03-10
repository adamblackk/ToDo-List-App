package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.ToDoDao
import com.example.todoapp.data.models.RecyclerData
import com.example.todoapp.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData:LiveData<List<ToDoData>> =toDoDao.getAllData()
    val sortByHighPriority:LiveData<List<ToDoData>> =toDoDao.sortByHighPriority()
    val sortByLowPriority:LiveData<List<ToDoData>> =toDoDao.sortByLowPriority()

    val getAllRecyclerData:LiveData<List<RecyclerData>> =toDoDao.getAllRecyclerData()
    // recyclerDataislemleri
    suspend fun insertRecyclerData(recyclerData: RecyclerData){
        toDoDao.insertRecyclerData(recyclerData)
    }
    suspend fun deleteRecyclerItem(recyclerData: RecyclerData){
        toDoDao.deleteRecyclerItem(recyclerData)
    }
    suspend fun deleteAllRecyclerItem(){
        toDoDao.deleteAllRecyclerItem()
    }

// tododata islemleri
    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData){
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAllItem(){
        toDoDao.deleteAllItem()
    }
    fun searchData(searchQuery:String):LiveData<List<ToDoData>>{
        return toDoDao.searchData(searchQuery)
    }
}