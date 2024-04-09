package com.erendogan6.dotoday.data.datasource

import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.data.room.DoTodayDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoTodayDataSource(val doTodayDao: DoTodayDao) {
    suspend fun saveToDo(toDo: ToDo) {
        doTodayDao.saveToDo(toDo)
    }

    suspend fun updateWorkList(workList: WorkList) {
        doTodayDao.updateWorkList(workList)
    }

    suspend fun updateToDo(toDo: ToDo) {
        doTodayDao.updateToDo(toDo)
    }

    suspend fun deleteToDo(toDo: ToDo) {
        doTodayDao.deleteToDo(toDo)
    }

    suspend fun searchToDo(searchText: String, workListId: Int): List<ToDo> {
        return doTodayDao.searchToDo(searchText, workListId)
    }

    suspend fun getToDosForWorkList(workListId: Int): List<ToDo> = withContext(Dispatchers.IO) {
        return@withContext doTodayDao.getToDosForWorkList(workListId)
    }

    suspend fun getCompletedTodos(workListId: Int): List<ToDo> = withContext(Dispatchers.IO) {
        return@withContext doTodayDao.getCompletedToDos(workListId)
    }

    suspend fun getNonCompletedToDos(workListId: Int): List<ToDo> = withContext(Dispatchers.IO) {
        return@withContext doTodayDao.getNonCompletedToDos(workListId)
    }

    suspend fun insertWorkList(workList: WorkList) {
        return doTodayDao.insertWorkList(workList)
    }

    suspend fun getAllWorkLists(): List<WorkList> = withContext(Dispatchers.IO) {
        return@withContext doTodayDao.getAllWorkLists()
    }

    suspend fun deleteWorkList(workList: WorkList) {
        return doTodayDao.deleteWorkList(workList)
    }
}