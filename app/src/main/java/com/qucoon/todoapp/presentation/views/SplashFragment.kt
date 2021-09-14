package com.qucoon.todoapp.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.qucoon.todoapp.R
import com.qucoon.todoapp.data.database.pref.AppPrefs
import com.qucoon.todoapp.data.database.pref.PrefHelper
import com.qucoon.todoapp.presentation.utils.delayFor
import androidx.appcompat.app.AppCompatActivity
import com.qucoon.todoapp.presentation.base.BaseFragment


class SplashFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delayFor(3000){
            findNavController().navigate(R.id.navigateToItemsFragment)
        }
    }
}