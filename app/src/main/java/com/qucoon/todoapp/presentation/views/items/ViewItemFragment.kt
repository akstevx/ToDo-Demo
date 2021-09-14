package com.qucoon.todoapp.presentation.views.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.qucoon.todoapp.R
import com.qucoon.todoapp.databinding.FragmentViewItemBinding
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.presentation.base.BaseFragment
import com.qucoon.todoapp.presentation.utils.capitalizeWords

class ViewItemFragment : BaseFragment() {
    private var binding: FragmentViewItemBinding? = null
    private val args: ViewItemFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewItemBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateViews()
        binding?.btnEditItem?.setOnClickListener {
            val action = ViewItemFragmentDirections.navigateToEditItemFragment(itemTitle = args.itemTitle, itemDate = args.itemDate,
                itemDescription = args.itemDescription, itemPriority = args.priority, id = it.id,)
            findNavController().navigate(action)
        }
    }

    private fun updateViews() {
        binding?.txtTitle?.text = args.itemTitle.capitalizeWords()
        binding?.txtDescription?.text =  args.itemDescription.capitalize()
        binding?.txtItemTag?.text = args.tag.capitalizeWords()
        binding?.txtDueDate?.text = args.itemDate

        when(args.priority){
            Priority.HIGH -> binding?.ivPriority?.setImageResource(R.drawable.high_priority_ic)
            Priority.MEDIUM -> binding?.ivPriority?.setImageResource(R.drawable.mid_priority_ic)
            Priority.LOW -> binding?.ivPriority?.setImageResource(R.drawable.low_priority_ic)
        }
    }
}