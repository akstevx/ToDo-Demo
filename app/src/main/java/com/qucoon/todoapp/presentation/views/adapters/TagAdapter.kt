package com.qucoon.todoapp.presentation.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.qucoon.todoapp.R
import com.qucoon.todoapp.domain.model.ItemTag
import com.qucoon.todoapp.presentation.utils.GenericViewHolder
import com.qucoon.todoapp.presentation.viewmodels.ItemsViewModel


class TagAdapter(private val listOfTags: MutableList<ItemTag.Tag>, private val viewModel: ItemsViewModel) :
    RecyclerView.Adapter<GenericViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view, context,viewModel)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<*>, position: Int) {
        val tag = listOfTags[position]
        when(holder){
            is TagViewHolder -> tag.let { holder.bind(it) }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return listOfTags.size
    }


    class TagViewHolder(val view: View, val context:Context, val viewModel: ItemsViewModel) : GenericViewHolder<ItemTag.Tag>(view) {
        private val tagName : TextView = view.findViewById(R.id.txtItem)
        private val container : ConstraintLayout = view.findViewById(R.id.container)

        override fun bind(tag: ItemTag.Tag) {
            tagName.text =  tag.tagName
            container.setOnClickListener {
                tag.isSelected = !tag.isSelected
                when(tag.isSelected){
                    true -> {
                        viewModel.tagSelectedListener.value = tag
                        selectedBg(container, tagName)
                    }
                    else -> defaultBg(container, tagName)
                }
            }
        }

        fun defaultBg(container : ConstraintLayout, tagName : TextView) {
            container.setBackgroundColor(context.resources.getColor(R.color.white))
            tagName.setTextColor(context.resources.getColor(R.color.light))
        }

        fun selectedBg(container : ConstraintLayout, tagName : TextView) {
            container.setBackgroundColor(context.resources.getColor(R.color.light))
            tagName.setTextColor(context.resources.getColor(R.color.white))
        }
    }
}
