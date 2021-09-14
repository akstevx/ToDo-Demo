package com.qucoon.todoapp.domain.usecase

import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.repository.ItemRepository
import javax.inject.Inject

interface EditItemUseCase {
    suspend fun createItem(item: Item)
    suspend fun updateItem(toDoData: Item)
}

open class EditItemUseCaseImpl @Inject constructor(
    private val itemRepository: ItemRepository
) : EditItemUseCase {
    override suspend fun createItem(item: Item) {
        itemRepository.insertItem(item)
    }

    override suspend fun updateItem(toDoData: Item) {
        itemRepository.updateData(toDoData)
    }
}