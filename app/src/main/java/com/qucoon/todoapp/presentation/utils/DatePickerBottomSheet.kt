package com.qucoon.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.qucoon.todoapp.databinding.FragmentDatePickerBottomSheet1Binding
import com.qucoon.todoapp.presentation.utils.*
import com.qucoon.todoapp.presentation.viewmodels.SharedViewModel

class DatePickerBottomSheet : BottomSheetDialogFragment() {
    private val args:DatePickerBottomSheetArgs by navArgs()
    private var binding: FragmentDatePickerBottomSheet1Binding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val dateFormat by lazy { args.dateFormat }
    private val titleText by lazy { args.title }
    private val date by lazy { args.date }
    private val dateType by lazy { args.dateType }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDatePickerBottomSheet1Binding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        binding?.datePicker?.setToTodayDate()
        binding?.textViewTitle?.text = titleText
        when(dateType){
            DateType.MIN_DATE -> args.dateType.let { binding?.datePicker?.minDate = date }
            DateType.MAX_DATE -> args.dateType.let { binding?.datePicker?.maxDate = date }
        }

        binding?.buttonContinue?.setOnClickListener {
            binding?.datePicker?.getDate(dateFormat)?.let { date ->
                sharedViewModel.datePickedListener.value = date
            }
            dialog?.dismiss()
        }
    }
}

enum class DateType{
    MAX_DATE,MIN_DATE
}