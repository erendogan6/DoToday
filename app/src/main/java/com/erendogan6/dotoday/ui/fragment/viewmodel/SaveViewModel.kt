package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.ViewModel
import com.erendogan6.dotoday.data.entity.ToDo
import com.erendogan6.dotoday.data.repo.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveViewModel @Inject constructor(var repo: DoTodayRepository): ViewModel()  {
    fun save(toDo: ToDo){
        CoroutineScope(Dispatchers.Main).launch {
            repo.save(toDo)
        }
    }

}