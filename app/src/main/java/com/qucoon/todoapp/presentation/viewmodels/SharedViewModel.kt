package com.qucoon.todoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.qucoon.todoapp.domain.usecase.EditItemUseCase
import com.qucoon.todoapp.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val editItemUseCaseImpl: EditItemUseCase
): ViewModel()  {
    var datePickedListener = SingleLiveEvent<String>()
    var errorListener = SingleLiveEvent<String>()
}