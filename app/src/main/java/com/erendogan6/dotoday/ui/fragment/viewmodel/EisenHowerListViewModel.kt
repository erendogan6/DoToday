package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.repository.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel class EisenHowerListViewModel @Inject constructor(private val repo: DoTodayRepository) : ViewModel() {

    private val _todos = MutableLiveData<List<ToDo>?>()
    val todos: MutableLiveData<List<ToDo>?> get() = _todos
    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean> get() = _dataLoaded
    private var lastOpenedFragment: String? = null

    init {
        loadAllTodos()
    }

    private fun loadAllTodos() {
        viewModelScope.launch {
            val allToDos = withContext(Dispatchers.IO) { repo.getAllToDos() }
            _todos.postValue(allToDos)
            _dataLoaded.postValue(true)
        }
    }

    fun filterTodosForCategory(category: String) {
        viewModelScope.launch {
            lastOpenedFragment = category
            val filteredTodos = when (category) {
                "important_and_urgent" -> todos.value?.filter { it.isUrgent() && it.isImportant() && !it.isCompleted }
                "important_and_not_urgent" -> todos.value?.filter { !it.isUrgent() && it.isImportant() && !it.isCompleted }
                "not_important_and_urgent" -> todos.value?.filter { it.isUrgent() && !it.isImportant() && !it.isCompleted }
                "not_important_and_not_urgent" -> todos.value?.filter { !it.isUrgent() && !it.isImportant() && !it.isCompleted }
                else -> listOf()
            }
            if (filteredTodos != null) {
                _todos.postValue(filteredTodos.sortedBy { it.dueDate })
            }
            _dataLoaded.postValue(false)
        }
    }

    fun deleteToDoAndUpdateView(toDo: ToDo) = viewModelScope.launch {
        repo.deleteToDo(toDo)
        loadAllTodos()
        filterTodosForCategory(lastOpenedFragment!!)
    }

    fun updateToDoAndUpdateView(toDo: ToDo) = viewModelScope.launch {
        repo.updateToDo(toDo)
        loadAllTodos()
        filterTodosForCategory(lastOpenedFragment!!)
    }

    private fun ToDo.isUrgent(): Boolean {
        val currentTime = System.currentTimeMillis()
        val oneWeekMillis = 7 * 24 * 60 * 60 * 1000
        return dueDate?.let { dueDate -> (dueDate - currentTime) <= oneWeekMillis } ?: false
    }

    private fun ToDo.isImportant(): Boolean {
        return priority == "high"
    }
}
