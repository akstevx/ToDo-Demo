package com.qucoon.todoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qucoon.todoapp.data.Constants
import com.qucoon.todoapp.data.database.dao.ItemDao
import com.qucoon.todoapp.domain.model.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
//@TypeConverters(Converter::class)
abstract class ItemRoomDatabase: RoomDatabase()  {
    abstract fun itemDao(): ItemDao

}
