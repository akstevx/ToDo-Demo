package com.qucoon.todoapp.domain.model

import androidx.lifecycle.LiveData
import com.qucoon.todoapp.R

object ItemTag {
    data class Tag(val tagName:String, var isSelected:Boolean = false)

    val listOfTags = listOf<Tag>(
        Tag("Work"),
        Tag("Personal"),
        Tag("Chores"),
        Tag("Recreational"),
        Tag("Miscellaneous")
    )
}