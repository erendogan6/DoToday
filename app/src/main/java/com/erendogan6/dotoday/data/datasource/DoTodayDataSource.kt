package com.erendogan6.dotoday.data.datasource

import com.erendogan6.dotoday.data.entity.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoTodayDataSource {
    suspend fun save(name:String){
        println("ToDo Saved")
    }

    suspend fun update(id:Int,name: String){
        println("ToDo Updated")
    }

    suspend fun delete(id:Int){
        println("ToDo Deleted")
    }

    suspend fun loadToDos() : ArrayList<ToDo> = withContext(Dispatchers.IO){
        val toDoList = ArrayList<ToDo>()
        var todo1 = ToDo(0,"Spor","Antrenman",false,1L)
        var todo2 = ToDo(0,"Ders","Antrenman",false,1L)
        var todo3 = ToDo(0,"Alışveriş","Antrenman",false,1L)
        toDoList.add(todo1)
        toDoList.add(todo2)
        toDoList.add(todo3)
        return@withContext toDoList
    }

    suspend fun search(searchText: String) : ArrayList<ToDo> = withContext(Dispatchers.IO){
        val toDoList = ArrayList<ToDo>()
        var todo1 = ToDo(0,"Spor","Antrenman",false,1L)
        toDoList.add(todo1)
        return@withContext toDoList
    }
}