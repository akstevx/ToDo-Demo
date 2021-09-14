package com.qucoon.todoapp.presentation.views.items

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qucoon.todoapp.R
import com.qucoon.todoapp.databinding.FragmentItemsBinding
import com.qucoon.todoapp.domain.model.Item
import com.qucoon.todoapp.domain.model.ItemTag
import com.qucoon.todoapp.domain.model.Priority
import com.qucoon.todoapp.presentation.base.BaseFragment
import com.qucoon.todoapp.presentation.utils.*
import com.qucoon.todoapp.presentation.viewmodels.ItemsViewModel
import com.qucoon.todoapp.presentation.views.adapters.ItemAdapter
import com.qucoon.todoapp.presentation.views.adapters.TagAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private var binding: FragmentItemsBinding ? = null
    private val viewModel: ItemsViewModel by activityViewModels()
    private lateinit var currentItems: List<Item>
    private val showEmptyState: MutableLiveData<Boolean> = MutableLiveData()
    private val itemAdapter: ItemAdapter by lazy { ItemAdapter(mutableListOf(),showEmptyState, viewModel) }
    private val tags = ItemTag.listOfTags.toMutableList()
    private val tagAdapter: TagAdapter by lazy { TagAdapter(tags,viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateViews()
    }

    private fun updateViews(){
        setUpTags()
        setObservers()

        binding?.btnAddNewTask?.setOnClickListener {
            findNavController().navigate(R.id.navigateToNewItemFragment)
        }

        binding?.fbAddNewTask?.setOnClickListener {
            findNavController().navigate(R.id.navigateToNewItemFragment)
        }
    }

    private fun setObservers(){
        viewModel.fetchAllItems(lifecycle = this)

        viewModel.items.observe(viewLifecycleOwner){
            currentItems = it
            itemAdapter.onNewData(it)
            setUpItemsRecycler()
        }

        showEmptyState.observe(viewLifecycleOwner){
            showEmptyState(it)
        }

        viewModel.itemSelectedListener.observe(viewLifecycleOwner){
            val action = ItemsFragmentDirections.navigateToViewItemFragment(itemDate = getPatternFromDate(date = it.date, outputPattern = "EEE, dd MMM yyyy",inputPattern = DefaultDateConfiguration.defaultDateFormat),
                itemDescription = it.description,id = it.id, itemTitle = it.title, priority = it.priority, tag = it.tag?: "Empty")
            findNavController().navigate(action)
        }
    }

    private fun showEmptyState(value:Boolean){
        when(value){
            true -> {
                binding?.fbAddNewTask?.hide()
                binding?.recyclerItems?.hide()
                binding?.group?.show()
            }
            false -> {
                binding?.fbAddNewTask?.show()
                binding?.recyclerItems?.show()
                binding?.group?.hide()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmDeletion()
            R.id.menu_priority_high -> viewModel.fetchAllItemsByPriority(viewLifecycleOwner, priority = Priority.HIGH)
            R.id.menu_priority_low ->  viewModel.fetchAllItemsByPriority(viewLifecycleOwner, priority = Priority.LOW)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            searchQuery(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            searchQuery(newText)
        }
        return true
    }

    private fun searchQuery(query: String){
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner){
            if(it.isNotEmpty()) viewModel.items.value = it else viewModel.fetchAllItems(lifecycle = this)
        }
    }

    private fun setUpItemsRecycler() {
        binding?.recyclerItems?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding?.recyclerItems?.adapter = itemAdapter
        binding?.recyclerItems?.scheduleLayoutAnimation()

        // Swipe to Delete
        swipeToDelete(binding?.recyclerItems!!, itemAdapter)
    }


    private fun swipeToDelete(recyclerView: RecyclerView, adapter: ItemAdapter) {
        val swipeToDeleteCallback = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = currentItems[viewHolder.adapterPosition]
                // Delete Item
                viewModel.deleteItem(deletedItem, viewLifecycleOwner)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore Deleted Item
                restoreDeletedItem(viewHolder.itemView, deletedItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: Item) {
        showSnackBarWithAction(view = view, text = "Deleted '${deletedItem.title}' on ToDo"){
            viewModel.createItem(deletedItem)
        }
    }

    private fun confirmDeletion() {
        hideKeyboard()
        showGenericDialogBox(header = "Delete Items", body = "Are you sure you want to \ndelete All items?\n please note this action cannot be undone"){
            viewModel.deleteAll(viewLifecycleOwner)
        }
    }

    private fun setUpTags(){
        binding?.recyclerTags?.layoutManager = GridLayoutManager(requireContext(), tags.size)
        binding?.recyclerTags?.adapter = tagAdapter

        viewModel.tagSelectedListener.observe(viewLifecycleOwner){
            searchQuery(it.tagName)
        }
    }
}