package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.models.RecyclerData
import com.example.todoapp.data.models.ToDoData

@Dao
interface ToDoDao {
    @Query("SELECT * FROM TODO_TABLE ORDER BY id ASC")
    fun getAllData():LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteItem(toDoData: ToDoData)

    @Query("DELETE FROM TODO_TABLE")
    suspend fun deleteAllItem()

    @Query("SELECT * FROM TODO_TABLE WHERE title LIKE :searchQuery")
    fun searchData(searchQuery:String):LiveData<List<ToDoData>>

    @Query("SELECT * FROM TODO_TABLE ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority():LiveData<List<ToDoData>>

    @Query("SELECT * FROM TODO_TABLE ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority():LiveData<List<ToDoData>>

    //recyclerData işlemleri
    @Query("SELECT * FROM recyclerData ORDER BY id ASC")
    fun getAllRecyclerData():LiveData<List<RecyclerData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecyclerData(recyclerData: RecyclerData)

    @Delete
    suspend fun deleteRecyclerItem(recyclerData: RecyclerData)

    @Query("DELETE FROM recyclerData")
    suspend fun deleteAllRecyclerItem()


}