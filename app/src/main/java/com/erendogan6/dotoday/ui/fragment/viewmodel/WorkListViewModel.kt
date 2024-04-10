package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.data.repository.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel class WorkListViewModel @Inject constructor(private val repo: DoTodayRepository) : ViewModel() {
    var workLists = MutableLiveData<List<WorkList>>()

    fun deleteWorkList(workList: WorkList) {
        viewModelScope.launch {
            repo.deleteWorkList(workList)
            getWorkList()
        }
    }

    fun getWorkList() {
        viewModelScope.launch {
            val lists = withContext(Dispatchers.IO) { repo.getAllWorkLists() }
            workLists.value = lists
        }
    }

    fun saveWorkList(workList: WorkList, onCompleted: () -> Unit) {
        viewModelScope.launch {
            repo.insertWorkList(workList)
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        }
    }

    fun updateWorkLists(workList: WorkList, onCompleted: () -> Unit) {
        viewModelScope.launch {
            repo.updateWorkList(workList)
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        }
    }

    fun calculateCompletionPercentage(workListId: Int): LiveData<Int> {
        val completionLiveData = MutableLiveData<Int>()

        viewModelScope.launch {
            val todos = repo.getToDosForWorkList(workListId)
            val completedTodos = todos.filter { it.isCompleted }
            val completionPercentage = if (todos.isNotEmpty()) (completedTodos.size * 100 / todos.size) else 0
            completionLiveData.postValue(completionPercentage)
        }

        return completionLiveData
    }
}