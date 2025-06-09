package com.house.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            items = List(20) { index ->
                ListItem(id = index, title = "Item $index", isHearted = false)
            }
        )
    )
    val uiState: StateFlow<UiState> = _uiState

    fun toggleHeart(itemId:Int){
       viewModelScope.launch {
           for(i in 1..100){
               _uiState.update{ current->
                   val updatedList = current.items.map {
                       if(it.id == itemId){
                           it.copy(progress = i)
                       }else{
                           it
                       }
                   }
                   current.copy(items = updatedList)
               }
               delay(16)
           }

           _uiState.update { current ->
               val updatedList = current.items.map { item ->
                   if (item.id == itemId) {
                       item.copy(isHearted = !item.isHearted, progress = 0)
                   } else {
                       item
                   }
               }
               current.copy(items = updatedList)
           }

       }
    }


    /** Delete the item with the given ID. */
    fun deleteItem(itemId: Int) {
        _uiState.update { current ->
            val filtered = current.items.filter { it.id != itemId }
            current.copy(items = filtered)
        }
    }

    /**
     * Move the item with [itemId] one position up (toward index = 0),
     * if it is not already at index 0.
     */
    fun moveItemUp(itemId: Int) {
        _uiState.update { current ->
            // Work on a mutable copy for swapping
            val listCopy = current.items.toMutableList()
            // Find the index of the item
            val index = listCopy.indexOfFirst { it.id == itemId }
            if (index > 0) {
                // Swap with the previous element
                val temp = listCopy[index - 1]
                listCopy[index - 1] = listCopy[index]
                listCopy[index] = temp
            }
            current.copy(items = listCopy.toList())
        }
    }

    /**
     * Move the item with [itemId] one position down (toward the end of the list),
     * if it is not already at the last index.
     */
    fun moveItemDown(itemId: Int) {
        _uiState.update { current ->
            val listCopy = current.items.toMutableList()
            val index = listCopy.indexOfFirst { it.id == itemId }
            if (index >= 0 && index < listCopy.size - 1) {
                // Swap with the next element
                val temp = listCopy[index + 1]
                listCopy[index + 1] = listCopy[index]
                listCopy[index] = temp
            }
            current.copy(items = listCopy.toList())
        }
    }
}
