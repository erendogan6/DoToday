package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erendogan6.dotoday.data.entity.ToDo
import com.erendogan6.dotoday.data.repo.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var repo: DoTodayRepository) : ViewModel() {
    var toDoList = MutableLiveData<List<ToDo>>()

    init {
        loadToDos()
    }

    fun delete(toDo: ToDo){
        CoroutineScope(Dispatchers.Main).launch {
            repo.delete(toDo)
            loadToDos()
        }
    }

     fun loadToDos(){
         CoroutineScope(Dispatchers.Main).launch {
             toDoList.value = repo.loadToDos()
         }
     }

    fun search(searchText:String){
        CoroutineScope(Dispatchers.Main).launch {
            toDoList.value = repo.search(searchText)
        }
    }
}