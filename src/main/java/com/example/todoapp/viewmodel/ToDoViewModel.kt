package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.ToDoDatabase
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application):AndroidViewModel(application) {

    private val toDoDao=ToDoDatabase.getDatabase(application).toDoDao()
    private val repository:ToDoRepository = ToDoRepository(toDoDao)

     val getAllData:LiveData<List<ToDoData>> = repository.getAllData
    val sortByHighPriority:LiveData<List<ToDoData>> =repository.sortByHighPriority
    val sortByLowPriority:LiveData<List<ToDoData>> =repository.sortByLowPriority

     fun insertData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }



    }
    fun updateData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAllItem(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllItem()
        }
    }
    fun searchData(searchQuery:String):LiveData<List<ToDoData>> {
        return repository.searchData(searchQuery)
    }
}