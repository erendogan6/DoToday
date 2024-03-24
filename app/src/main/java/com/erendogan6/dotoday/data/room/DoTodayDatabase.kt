package com.erendogan6.dotoday.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erendogan6.dotoday.data.entity.ToDo
import com.erendogan6.dotoday.utils.Converters

@Database(entities = [ToDo::class], version = 1)
@TypeConverters(Converters::class)
abstract class DoTodayDatabase : RoomDatabase() {
    abstract fun getDao():DoTodayDao
}