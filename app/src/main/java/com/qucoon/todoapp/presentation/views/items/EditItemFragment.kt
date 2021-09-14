package com.qucoon.todoapp.presentation.views.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.qucoon.todoapp.R
import com.qucoon.todoapp.databinding.FragmentEditItemBinding
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.ItemTag
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.presentation.base.BaseFragment
import com.qucoon.todoapp.presentation.utils.addNYearsToDate
import com.qucoon.todoapp.presentation.utils.DefaultDateConfiguration
import com.qucoon.todoapp.presentation.utils.getPatternFromDate
import com.qucoon.todoapp.presentation.viewmodels.ItemsViewModel
import com.qucoon.todoapp.presentation.views.adapters.TagAdapter
import java.util.*

class EditItemFragment : BaseFragment() {
    private val viewModel: ItemsViewModel by activityViewModels()
    private var binding: FragmentEditItemBinding ?= null
    private val args: EditItemFragmentArgs by navArgs()
    private var priority: Priority ?= null
    private var selectedTag:String? = null
    private var datePickedInOriginalFormat: String ?= null
    private val tags = ItemTag.listOfTags.toMutableList()
    private val tagAdapter: TagAdapter by lazy { TagAdapter(ItemTag.listOfTags.toMutableList(),viewModel) }
    private val requiredDate: Long by lazy {
        val requiredDate = addNYearsToDate(Date(), 0)
        requiredDate.time
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditItemBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateViews()
        setObserver()
    }

    private fun updateViews() {
        setUpTags()
        binding?.etTaskTitle?.setText(args.itemTitle)
        binding?.etDescription?.setText(args.itemDescription)
        binding?.etTaskDate?.setText(args.itemDate)
        updateCheckbox(priority = args.itemPriority)
        editItem()

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

    private fun setObserver(){
        sharedViewModel.datePickedListener.observe(viewLifecycleOwner){
            datePickedInOriginalFormat = it
            binding?.etTaskDate?.setText(
                getPatternFromDate(date = it, outputPattern = "EEE, dd MMM yyyy",
                inputPattern = DefaultDateConfiguration.defaultDateFormat)
            )
        }
    }

    private fun editItem(){
        binding?.btnDone?.setOnClickListener {
            if(checkFields()){
                val item = Item(
                    id = args.id,
                    title = binding?.etTaskTitle?.text.toString(),
                    description = binding?.etDescription?.text.toString(),
                    priority = priority ?: args.itemPriority,
                    tag = selectedTag,
                    date = datePickedInOriginalFormat ?: getPatternFromDate(date = args.itemDate, inputPattern = "EEE, dd MMM yyyy",
                        outputPattern = DefaultDateConfiguration.defaultDateFormat)
                )
                viewModel.createItem(item)
                sharedViewModel.errorListener.value = "${args.itemTitle} has been Edited"
                findNavController().popBackStack()
            } else sharedViewModel.errorListener.value = "All fields are required, please fill"
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

    private fun checkFields():Boolean{
        return binding?.etTaskTitle?.text.toString() != "" && binding?.etDescription?.text.toString() != "" &&
                binding?.etTaskDate?.text.toString() != ""
    }

    private fun setUpTags(){
        binding?.recyclerTags?.layoutManager = GridLayoutManager(requireContext(), tags.size)
        binding?.recyclerTags?.adapter = tagAdapter

        viewModel.tagSelectedListener.observe(viewLifecycleOwner){
            selectedTag = it.tagName
        }
    }
}