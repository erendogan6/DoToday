package com.erendogan6.dotoday.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.model.WorkList

@Dao interface DoTodayDao {

    @Update suspend fun updateToDo(toDo: ToDo)

    @Insert suspend fun saveToDo(toDo: ToDo)

    @Delete suspend fun deleteToDo(toDo: ToDo)

    @Update suspend fun updateWorkList(workList: WorkList)

    @Query("SELECT * FROM ToDo WHERE title LIKE '%' || :searchText || '%' AND workListId = :workListId") suspend fun searchToDo(
        searchText: String, workListId: Int
    ): List<ToDo>

    @Query("SELECT * FROM ToDo WHERE workListId = :workListId") suspend fun getToDosForWorkList(workListId: Int): List<ToDo>

    @Query("SELECT * FROM ToDo WHERE workListId = :workListId AND isCompleted=1 ") suspend fun getCompletedToDos(workListId: Int): List<ToDo>

    @Query("SELECT * FROM ToDo WHERE workListId = :workListId AND isCompleted=0 ") suspend fun getNonCompletedToDos(workListId: Int): List<ToDo>

    @Insert suspend fun insertWorkList(workList: WorkList)

    @Query("SELECT * FROM work_list") suspend fun getAllWorkLists(): List<WorkList>

    @Delete suspend fun deleteWorkList(workList: WorkList)

}