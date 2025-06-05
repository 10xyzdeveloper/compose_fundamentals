package com.house.myapplication

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(): ViewModel(){

    private val _uiState = MutableStateFlow(UiState(items = List(20){ index->
        ListItem(id = index, title = "Item $index", isHearted = false)
    }))


    val uiState: StateFlow<UiState> = _uiState




}