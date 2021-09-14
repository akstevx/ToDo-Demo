package com.qucoon.todoapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.qucoon.todoapp.R
import com.qucoon.todoapp.presentation.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.generic_dialog_box.view.*

open class BaseFragment: Fragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSharedViewModel()
    }

    private fun observeSharedViewModel(){
        sharedViewModel.errorListener.observe(this){
//            showToast(it)
            showSnackBar(this.requireView(), it)
        }
    }

    fun showSnackBar(view: View, text:String,){
        val snackBar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        snackBar.show()
    }

    fun showSnackBarWithAction(view: View, text:String, action: ()-> Unit) {
        val snackBar = Snackbar.make(view, text, Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") { action() }
        snackBar.setActionTextColor(resources.getColor(R.color.purple_700))
        snackBar.show()
    }

    fun showGenericDialogBox(header: String, body:String, action: () -> Unit){

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.generic_dialog_box, null)

        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(requireContext()).setView(messageBoxView)

        //setting text values
        messageBoxView.message_box_header.text = header
        messageBoxView.message_box_content.text = body
        val  messageBoxInstance = messageBoxBuilder.show()

        messageBoxView.btnYes.setOnClickListener {
            action()
            messageBoxInstance.dismiss()
        }

        messageBoxView.btnCancel.setOnClickListener {
            //close dialog
            messageBoxInstance.dismiss()
        }
    }


    fun showToast(message:String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}