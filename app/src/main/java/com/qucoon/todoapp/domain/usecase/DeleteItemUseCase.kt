package com.qucoon.todoapp.domain.usecase

import androidx.lifecycle.LiveData
import com.qucoon.todoapp.domain.UseCase
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.Order
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.domain.repository.ItemRepository
import javax.inject.Inject



interface DeleteItemUseCase{
    suspend fun deleteItem(item:Item)
    suspend fun deleteAll()
}
open class DeleteItemUseCaseImpl @Inject constructor(
    private val itemRepository: ItemRepository
) : DeleteItemUseCase {

    override suspend fun deleteItem(item: Item) {
        itemRepository.deleteItem(item)
    }

    override suspend fun deleteAll() {
        itemRepository.deleteAll()
    }
}