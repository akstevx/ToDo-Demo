package com.qucoon.todoapp.domain.usecase

import androidx.lifecycle.LiveData
import com.qucoon.todoapp.domain.UseCase
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.Order
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.domain.repository.ItemRepository
import javax.inject.Inject

open class GetItemsUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) : UseCase<GetItemsUseCase, LiveData<List<Item>>>() {

    override fun onCleared() {
        super.onCleared()
    }

    override fun createObservable(params: GetItemsUseCase?): LiveData<List<Item>> {
        return itemRepository.fetchAllData()
    }

    override fun createPriorityObservable(params: GetItemsUseCase?, priority: Priority): LiveData<List<Item>> {
        return when(priority){
                Priority.HIGH -> itemRepository.sortByHighPriority()
                Priority.LOW -> itemRepository.sortByLowPriority()
                else -> itemRepository.sortByHighPriority()
        }
    }

    override fun createOrderedObservable(
        params: GetItemsUseCase?,
        order: Order
    ): LiveData<List<Item>> {
        return when(order){
            Order.ASCENDING -> itemRepository.fetchAllData()
            Order.DESCENDING -> itemRepository.fetchAllDataDescending()
        }
    }

}