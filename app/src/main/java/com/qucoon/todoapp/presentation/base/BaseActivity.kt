package com.qucoon.todoapp.presentation.base

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

open class BaseActivity : AppCompatActivity() {
    lateinit var navController: NavController

}