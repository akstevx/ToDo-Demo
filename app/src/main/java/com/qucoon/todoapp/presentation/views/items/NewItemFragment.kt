package com.qucoon.todoapp.presentation.views.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.qucoon.todoapp.R
import com.qucoon.todoapp.databinding.FragmentNewItemBinding
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.ItemTag
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.presentation.base.BaseFragment
import com.qucoon.todoapp.presentation.utils.*
import com.qucoon.todoapp.presentation.viewmodels.ItemsViewModel
import com.qucoon.todoapp.presentation.views.adapters.TagAdapter
import java.util.*

class NewItemFragment : BaseFragment() {
    private val viewModel: ItemsViewModel by activityViewModels()
    private var binding: FragmentNewItemBinding? = null
    private var selectedTag:String? = null
    private var priority: Priority ?= null
    lateinit var datePickedInOriginalFormat: String
    private val tags = ItemTag.listOfTags.toMutableList()
    private val tagAdapter: TagAdapter by lazy { TagAdapter(tags,viewModel) }
    //get current date
    private val requiredDate: Long by lazy {
        val requiredDate = addNYearsToDate(Date(), 0)
        requiredDate.time
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewItemBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateViews()
    }

    private fun updateViews(){
        setUpTags()
        setObserver()
        createItem()

        binding?.btnSelectDate?.setOnClickListener{
            val action = NewItemFragmentDirections.navigateToDateBottomsheet(dateFormat = DefaultDateConfiguration.defaultDateFormat,
                dateType = DefaultDateConfiguration.dateType, date = requiredDate, title = DefaultDateConfiguration.defaultTitle)
            findNavController().navigate(action)
        }

        binding?.btnSelectHigh?.setOnClickListener {
            priority = Priority.HIGH
            updateCheckbox(priority!!)
        }
        binding?.btnSelectMid?.setOnClickListener {
            priority = Priority.MEDIUM
            updateCheckbox(priority!!)
        }
        binding?.btnSelectLow?.setOnClickListener {
            priority = Priority.LOW
            updateCheckbox(priority!!)
        }
    }

    private fun createItem(){
        binding?.btnDone?.setOnClickListener {
            //check if fields are filled
            if(checkFields()){
                val item = Item(
                    id = 0,
                    title = binding?.etTaskTitle?.text.toString(),
                    description = binding?.etDescription?.text.toString(),
                    priority = priority!!,
                    tag = selectedTag,
                    date = datePickedInOriginalFormat
                )
                //insert item to dao
                viewModel.createItem(item)
                sharedViewModel.errorListener.value = "Item has been added to Todo list"
                findNavController().popBackStack()
            } else sharedViewModel.errorListener.value = "All fields are required, please fill"
        }
    }

    private fun checkFields():Boolean{
        return binding?.etTaskTitle?.text.toString() != "" && binding?.etDescription?.text.toString() != "" &&
                binding?.etTaskDate?.text.toString() != "" && priority != null
    }

    private fun setObserver(){
        sharedViewModel.datePickedListener.observe(viewLifecycleOwner){
            datePickedInOriginalFormat = it
            binding?.etTaskDate?.setText(getPatternFromDate(date = it, outputPattern = "EEE, dd MMM yyyy",
                inputPattern = DefaultDateConfiguration.defaultDateFormat))
        }
    }

    private fun updateCheckbox(priority: Priority){
        when(priority){
            Priority.HIGH -> {
                binding?.ivHighPriority?.setBackgroundResource(R.drawable.fill_checkbox_ic)
                binding?.ivMedPriority?.setBackgroundResource(R.drawable.empty_checkbox_ic)
                binding?.ivLowPriority?.setBackgroundResource(R.drawable.empty_checkbox_ic)
            }
            Priority.MEDIUM -> {
                binding?.ivHighPriority?.setBackgroundResource(R.drawable.empty_checkbox_ic)
                binding?.ivMedPriority?.setBackgroundResource(R.drawable.fill_checkbox_ic)
                binding?.ivLowPriority?.setBackgroundResource(R.drawable.empty_checkbox_ic)
            }
            Priority.LOW -> {
                binding?.ivHighPriority?.setBackgroundResource(R.drawable.empty_checkbox_ic)
                binding?.ivMedPriority?.setBackgroundResource(R.drawable.empty_checkbox_ic)
                binding?.ivLowPriority?.setBackgroundResource(R.drawable.fill_checkbox_ic)
            }
        }
    }

    private fun setUpTags(){
        binding?.recyclerTags?.layoutManager = GridLayoutManager(requireContext(), tags.size)
        binding?.recyclerTags?.adapter = tagAdapter

        viewModel.tagListener.observe(viewLifecycleOwner){
            selectedTag = it.first.tagName
        }
    }
}