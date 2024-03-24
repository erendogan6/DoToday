package com.erendogan6.dotoday.data.repo

import com.erendogan6.dotoday.data.datasource.DoTodayDataSource
import com.erendogan6.dotoday.data.entity.ToDo
import javax.inject.Inject

class DoTodayRepository @Inject constructor(
    private val dataSource: DoTodayDataSource) {
    suspend fun save(name:String) = dataSource.save(name)

    suspend fun update(id:Int,name: String) = dataSource.update(id,name)

    suspend fun delete(id:Int) = dataSource.delete(id)

    suspend fun loadToDos() : ArrayList<ToDo> = dataSource.loadToDos()

    suspend fun search(searchText:String): ArrayList<ToDo> = dataSource.search(searchText)

}