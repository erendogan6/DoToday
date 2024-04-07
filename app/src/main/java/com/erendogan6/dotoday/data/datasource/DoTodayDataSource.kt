package com.erendogan6.dotoday.data.datasource

import androidx.room.Delete
import androidx.room.Insert
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.data.room.DoTodayDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoTodayDataSource(var doTodayDao: DoTodayDao) {
    suspend fun save(toDo: ToDo) {
        doTodayDao.save(toDo)
    }

    suspend fun updateWorkList(workList: WorkList) {
        doTodayDao.updateWorkList(workList)
    }

    suspend fun update(toDo: ToDo) {
        doTodayDao.update(toDo)
    }

    suspend fun delete(toDo: ToDo) {
        doTodayDao.delete(toDo)
    }


    suspend fun search(searchText: String): List<ToDo> {
        return doTodayDao.search(searchText)
    }

    suspend fun getToDosForWorkList(workListId: Int): List<ToDo> = withContext(Dispatchers.IO) {
        return@withContext doTodayDao.getToDosForWorkList(workListId)
    }

    @Insert
    suspend fun insertWorkList(workList: WorkList) {
        return doTodayDao.insertWorkList(workList)
    }

    suspend fun getAllWorkLists(): List<WorkList> = withContext(Dispatchers.IO) {
        return@withContext doTodayDao.getAllWorkLists()
    }

    @Delete
    suspend fun deleteWorkList(workList: WorkList) {
        return doTodayDao.deleteWorkList(workList)
    }
}