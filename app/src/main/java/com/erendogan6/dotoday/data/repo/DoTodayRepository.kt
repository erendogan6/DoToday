package com.erendogan6.dotoday.data.repo

import com.erendogan6.dotoday.data.datasource.DoTodayDataSource
import com.erendogan6.dotoday.data.entity.ToDo
import javax.inject.Inject

class DoTodayRepository @Inject constructor(
    private val dataSource: DoTodayDataSource) {
    suspend fun save(toDo: ToDo) = dataSource.save(toDo)

    suspend fun update(toDo: ToDo) = dataSource.update(toDo)

    suspend fun delete(toDo: ToDo) = dataSource.delete(toDo)

    suspend fun loadToDos() : List<ToDo> = dataSource.loadToDos()

    suspend fun search(searchText:String): List<ToDo> = dataSource.search(searchText)

}