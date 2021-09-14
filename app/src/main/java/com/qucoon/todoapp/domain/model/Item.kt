package com.qucoon.todoapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val priority: Priority,
    val description: String,
    val tag: String? = "Empty",
    val date: String,
): Serializable

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}

enum class Order {
    ASCENDING,
    DESCENDING,
}