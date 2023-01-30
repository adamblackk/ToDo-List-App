package com.example.todoapp.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.room.util.appendPlaceholders
import com.example.todoapp.data.ToDoDatabase
import com.example.todoapp.data.models.RecyclerData
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecyclerDataViewModel(application: Application):AndroidViewModel(application){

    private val toDoDao=ToDoDatabase.getDatabase(application).toDoDao()
    private val repository:ToDoRepository= ToDoRepository(toDoDao)

    val getAllRecyclerData:LiveData<List<RecyclerData>> =repository.getAllRecyclerData

    fun insertRecyclerData(recyclerData: RecyclerData){
        viewModelScope.launch(Dispatchers.IO) {

            repository.insertRecyclerData(recyclerData)

        }
    }

    fun deleteRecyclerData(recyclerData: RecyclerData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecyclerItem(recyclerData)
        }
    }

    fun deleteAllRecyclerDataItem(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllRecyclerItem()
        }
    }
}