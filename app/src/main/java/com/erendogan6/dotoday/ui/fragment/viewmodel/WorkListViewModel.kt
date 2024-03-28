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
    var workList = MutableLiveData<List<WorkList>>()
    fun deleteWorkList(workList: WorkList) {
        CoroutineScope(Dispatchers.Main).launch {
            repo.deleteWorkList(workList)
            getWorkList()
        }
    }

    fun getWorkList() {
        println("Viewmodel getworklist calisti")
        CoroutineScope(Dispatchers.Main).launch {
            println("Viewmodel coroutine getallwroklists calisti")
            workList.value = repo.getAllWorkLists()
        }
    }

    fun saveWorkList(workList: WorkList, onCompleted: () -> Unit) {
        println("Viewmodel saveworklist calisti")
        CoroutineScope(Dispatchers.IO).launch {
            println("viewmodel coroutine insertworklist calisti")
            repo.insertWorkList(workList)
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        }
    }

}