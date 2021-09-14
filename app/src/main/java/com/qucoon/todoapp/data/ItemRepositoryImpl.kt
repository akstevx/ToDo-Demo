package com.qucoon.todoapp.data

import androidx.lifecycle.LiveData
import com.qucoon.todoapp.data.database.ItemRoomDatabase
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.repository.ItemRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepositoryImpl @Inject constructor(
    private val appDatabase: ItemRoomDatabase,
) : ItemRepository {
    override fun fetchAllData(): LiveData<List<Item>> {
        return appDatabase.itemDao().getAllItemsAscending()
    }

    override fun fetchAllDataDescending(): LiveData<List<Item>> {
        return appDatabase.itemDao().getAllItemsDescending()
    }

    override fun sortByHighPriority(): LiveData<List<Item>> {
        return appDatabase.itemDao().sortByHighPriority()
    }

    override fun sortByLowPriority(): LiveData<List<Item>> {
        return appDatabase.itemDao().sortByLowPriority()
    }

    override suspend fun insertItem(toDoData: Item) {
        appDatabase.itemDao().insertItem(toDoData)
    }

    override suspend fun updateData(toDoData: Item) {
        appDatabase.itemDao().updateItem(toDoData)
    }

    override suspend fun deleteItem(toDoData: Item) {
        appDatabase.itemDao().deleteItem(toDoData.id)
    }

    override fun searchDatabase(searchQuery: String): LiveData<List<Item>> {
        return appDatabase.itemDao().searchDatabase(searchQuery)
    }

    override suspend fun deleteAll() {
        appDatabase.itemDao().deleteAll()
    }
}