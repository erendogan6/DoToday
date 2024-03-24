package com.erendogan6.dotoday.data.datasource

import com.erendogan6.dotoday.data.entity.ToDo
import com.erendogan6.dotoday.data.room.DoTodayDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoTodayDataSource (var doTodayDao : DoTodayDao) {
    suspend fun save(toDo: ToDo){
        doTodayDao.save(toDo)
    }

    suspend fun update(toDo: ToDo){
        doTodayDao.update(toDo)
    }

    suspend fun delete(toDo: ToDo){
        doTodayDao.delete(toDo)
    }

    suspend fun loadToDos() : List<ToDo> = withContext(Dispatchers.IO){
        return@withContext doTodayDao.loadToDos()
    }

    suspend fun search(searchText: String): List<ToDo> {
        return doTodayDao.search(searchText)
    }
}