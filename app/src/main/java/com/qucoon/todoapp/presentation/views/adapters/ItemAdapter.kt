package com.qucoon.todoapp.presentation.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.qucoon.todoapp.R
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.presentation.utils.GenericViewHolder
import com.qucoon.todoapp.presentation.utils.SwipeToDelete
import com.qucoon.todoapp.presentation.utils.getPatternFromDate
import com.qucoon.todoapp.presentation.viewmodels.ItemsViewModel

class ItemAdapter(private val listOfItems: MutableList<Item>, private val showEmptyState: MutableLiveData<Boolean>, private val viewModel:ItemsViewModel) : RecyclerView.Adapter<GenericViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<*>, position: Int) {
        val item = listOfItems[position]
        when(holder){
            is ItemViewHolder -> item.let { holder.bind(it) }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        showEmptyState.value = listOfItems.size == 0
        return listOfItems.size
    }

    fun onNewData(newData: List<Item>){
        listOfItems.clear()
        listOfItems.addAll(newData)
        showEmptyState.value = listOfItems.size == 0
        notifyDataSetChanged()
    }

    class ItemViewHolder(val view: View, val viewModel:ItemsViewModel) : GenericViewHolder<Item>(view) {
        private val title : TextView = view.findViewById(R.id.txtItemTitle)
        private val detail : TextView = view.findViewById(R.id.txtItemDetail)
        private val date : TextView = view.findViewById(R.id.txtItemDate)
        private val priority : ImageView = view.findViewById(R.id.ivItemPriority)
        private val container : ViewGroup = view.findViewById(R.id.itemContainer)

        override fun bind(item: Item) {
            title.text =  item.title
            detail.text = item.description
            date.text = getPatternFromDate(date = item.date, inputPattern = "dd MMM yyyy")
            when(item.priority){
                Priority.HIGH -> priority.setImageResource(R.drawable.high_priority_ic)
                Priority.MEDIUM -> priority.setImageResource(R.drawable.mid_priority_ic)
                Priority.LOW -> priority.setImageResource(R.drawable.low_priority_ic)
            }

            container.setOnClickListener{
                viewModel.itemSelectedListener.value = item
            }
        }
    }

}