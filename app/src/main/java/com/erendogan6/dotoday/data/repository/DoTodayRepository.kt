package com.erendogan6.dotoday.data.repository

import com.erendogan6.dotoday.data.datasource.DoTodayDataSource
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.model.WorkList
import javax.inject.Inject

class DoTodayRepository @Inject constructor(private val dataSource: DoTodayDataSource) {
    suspend fun update(toDo: ToDo) = dataSource.update(toDo)
    suspend fun save(toDo: ToDo) = dataSource.save(toDo)

    suspend fun delete(toDo: ToDo) = dataSource.delete(toDo)

    suspend fun search(searchText: String, workListId: Int): List<ToDo> =
        dataSource.search(searchText, workListId)

    suspend fun getToDosForWorkList(workListId: Int): List<ToDo> =
        dataSource.getToDosForWorkList(workListId)

    suspend fun getCompletedTodos(worlListId: Int): List<ToDo> =
        dataSource.getCompletedTodos(worlListId)

    suspend fun getNonCompletedTodos(worlListId: Int): List<ToDo> =
        dataSource.getNonCompletedToDos(worlListId)

    suspend fun insertWorkList(workList: WorkList) = dataSource.insertWorkList(workList)

    suspend fun getAllWorkLists(): List<WorkList> = dataSource.getAllWorkLists()

    suspend fun deleteWorkList(workList: WorkList) = dataSource.deleteWorkList(workList)

    suspend fun updateWorkList(workList: WorkList) = dataSource.updateWorkList(workList)
}