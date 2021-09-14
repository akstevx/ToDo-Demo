package com.qucoon.todoapp.data.database

import androidx.room.TypeConverter
import com.qucoon.todoapp.domain.model.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

}