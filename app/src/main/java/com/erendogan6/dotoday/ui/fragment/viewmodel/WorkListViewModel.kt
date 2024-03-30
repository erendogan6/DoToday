package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.data.repository.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class WorkListViewModel @Inject constructor(var repo: DoTodayRepository) : ViewModel() {
    var workLists = MutableLiveData<List<WorkList>>()

    fun deleteWorkList(workList: WorkList) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteWorkList(workList)
            getWorkList()
        }
    }

    fun getWorkList() {
        CoroutineScope(Dispatchers.Main).launch {
            workLists.value = repo.getAllWorkLists()
        }
    }

    fun saveWorkList(workList: WorkList, onCompleted: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.insertWorkList(workList)
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        }
    }

    fun update(workList: WorkList, onCompleted: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateWorkList(workList)
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        }
    }
}