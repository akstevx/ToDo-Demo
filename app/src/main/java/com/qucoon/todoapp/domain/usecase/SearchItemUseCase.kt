package com.qucoon.todoapp.domain.usecase

import androidx.lifecycle.LiveData
import com.qucoon.todoapp.domain.UseCase
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.Order
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.domain.repository.ItemRepository
import javax.inject.Inject


open class SearchItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) : UseCase<SearchItemUseCase.Params, LiveData<List<Item>>>()  {

    override fun createObservable(params: Params?): LiveData<List<Item>> {
        if(params != null){
            return itemRepository.searchDatabase(params.query)
        }
        return itemRepository.searchDatabase("")
    }

    override fun createPriorityObservable(
        params: Params?,
        priority: Priority
    ): LiveData<List<Item>> {
        TODO("Not yet implemented")
    }

    override fun createOrderedObservable(params: Params?, order: Order): LiveData<List<Item>> {
        TODO("Not yet implemented")
    }

    data class Params(val query: String)
}