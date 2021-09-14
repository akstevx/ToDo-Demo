package com.qucoon.todoapp.domain.repository

import androidx.lifecycle.LiveData
import com.qucoon.todoapp.domain.model.Item

interface ItemRepository {
    fun fetchAllData(): LiveData<List<Item>>
    fun fetchAllDataDescending(): LiveData<List<Item>>
    fun sortByHighPriority(): LiveData<List<Item>>
    fun sortByLowPriority(): LiveData<List<Item>>
    fun searchDatabase(searchQuery:String): LiveData<List<Item>>
    suspend fun insertItem(toDoData: Item)
    suspend fun updateData(toDoData: Item)
    suspend fun deleteItem(toDoData: Item)
    suspend fun deleteAll()
}