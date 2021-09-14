package com.qucoon.todoapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.qucoon.todoapp.domain.model.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM item_table ORDER BY id ASC")
    fun getAllItemsAscending(): LiveData<List<Item>>

    @Query("SELECT * FROM item_table ORDER BY id DESC")
    fun getAllItemsDescending(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(toDoData: Item)

    @Query("DELETE FROM item_table WHERE id == :id")
    suspend fun deleteItem(id: Int)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(itemData: Item){
        deleteItem(itemData.id)
        insertItem(itemData)
    }

    @Query("DELETE FROM item_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM item_table WHERE title OR tag LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Item>>

    @Query("SELECT * FROM item_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<Item>>

    @Query("SELECT * FROM item_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<Item>>
}
