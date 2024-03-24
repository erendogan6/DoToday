package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.ViewModel
import com.erendogan6.dotoday.data.repo.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(var repo: DoTodayRepository) : ViewModel() {
    fun update(id:Int,name: String){
        CoroutineScope(Dispatchers.Main).launch {
            repo.update(id,name)
        }
    }

}