package com.erendogan6.dotoday.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.erendogan6.dotoday.data.entity.ToDo
@Dao
interface DoTodayDao {
    @Query("SELECT * FROM ToDo")
    suspend fun loadToDos():List<ToDo>

    @Insert
    suspend fun save(toDo: ToDo)

    @Delete
    suspend fun delete(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Query("SELECT * FROM ToDo WHERE title LIKE '%' || :searchText || '%'")
    suspend fun search(searchText: String): List<ToDo>

}