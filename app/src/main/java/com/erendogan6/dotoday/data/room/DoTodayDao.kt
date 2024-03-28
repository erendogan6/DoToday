package com.erendogan6.dotoday.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.model.WorkList

@Dao
interface DoTodayDao {
    @Insert
    suspend fun save(toDo: ToDo)

    @Delete
    suspend fun delete(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Query("SELECT * FROM ToDo WHERE title LIKE '%' || :searchText || '%'")
    suspend fun search(searchText: String): List<ToDo>

    @Query("SELECT * FROM ToDo WHERE workListId = :workListId")
    suspend fun getToDosForWorkList(workListId: Int): List<ToDo>

    @Insert
    suspend fun insertWorkList(workList: WorkList)

    @Query("SELECT * FROM work_list")
    suspend fun getAllWorkLists(): List<WorkList>

    @Delete
    suspend fun deleteWorkList(workList: WorkList)

}