package com.qucoon.todoapp.presentation.viewmodels

import androidx.lifecycle.*
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.ItemTag
import com.qucoon.todoapp.domain.model.Order
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.domain.usecase.*
import com.qucoon.todoapp.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val searchItemUseCase: SearchItemUseCase,
    private val deleteItemUseCaseImpl: DeleteItemUseCase,
    private val editItemUseCaseImpl: EditItemUseCase
): ViewModel()  {
    val items = MutableLiveData<List<Item>>()
    val itemSelectedListener = SingleLiveEvent<Item>()
    val tagListener = SingleLiveEvent<Pair<ItemTag.Tag, Int>>()

    fun searchDatabase(query: String) = searchItemUseCase.createObservable(params = SearchItemUseCase.Params(query))

    fun fetchAllItems(lifecycle: LifecycleOwner){
        getItemsUseCase.createObservable().observe(lifecycle){
            items.value = it
        }
    }

    fun fetchAllItemsByDate(lifecycle: LifecycleOwner, order: Order){
        when(order){
            Order.ASCENDING -> sortItemsByAscending(lifecycle)
            Order.DESCENDING -> sortItemsByDescending(lifecycle)
        }
    }

    private fun sortItemsByAscending(lifecycle: LifecycleOwner) =  getItemsUseCase.createOrderedObservable(order = Order.ASCENDING).observe(lifecycle){
        items.value = it }
    private fun sortItemsByDescending(lifecycle: LifecycleOwner) =  getItemsUseCase.createOrderedObservable(order = Order.DESCENDING).observe(lifecycle){
        items.value = it }

    fun fetchAllItemsByPriority(lifecycle: LifecycleOwner, priority: Priority){
        when(priority){
            Priority.HIGH -> sortItemsByHighPriority(lifecycle)
            Priority.LOW -> sortItemsByLowPriority(lifecycle)
            else -> sortItemsByHighPriority(lifecycle)
        }
    }

    private fun sortItemsByHighPriority(lifecycle: LifecycleOwner) =  getItemsUseCase.createPriorityObservable(priority = Priority.HIGH).observe(lifecycle){
        items.value = it }
    private fun sortItemsByLowPriority(lifecycle: LifecycleOwner) =  getItemsUseCase.createPriorityObservable(priority = Priority.LOW).observe(lifecycle){
        items.value = it }

    fun deleteItem(item:Item, lifecycle: LifecycleOwner){
        viewModelScope.launch(Dispatchers.IO) {
            deleteItemUseCaseImpl.deleteItem(item)
        }
        fetchAllItems(lifecycle)
    }


    fun editItem(item:Item){
        viewModelScope.launch (Dispatchers.IO){
            editItemUseCaseImpl.updateItem(item)
        }
    }

    fun deleteAll(lifecycle: LifecycleOwner){
        viewModelScope.launch(Dispatchers.IO) {
            deleteItemUseCaseImpl.deleteAll()
        }
        fetchAllItems(lifecycle)
    }

    fun createItem(item:Item){
        val item = Item (date = item.date, tag = item.tag, title = item.title, description = item.description, priority = item.priority, id = 0)
        viewModelScope.launch (Dispatchers.IO){
            editItemUseCaseImpl.createItem(item)
        }
    }

    fun sortByTags(tag: ItemTag.Tag, item:List<Item>){
        if(item.isNotEmpty()){
            items.value = item.filter { it.tag == tag.tagName }
        } else return
    }

}

