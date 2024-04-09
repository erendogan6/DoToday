package com.erendogan6.dotoday.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "ToDo", foreignKeys = [ForeignKey(
        entity = WorkList::class, parentColumns = arrayOf("id"), childColumns = arrayOf("workListId"), onDelete = ForeignKey.CASCADE
    )], indices = [Index(value = ["workListId"])]
) data class ToDo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false,
    @ColumnInfo(name = "dueDate") var dueDate: Long?,
    @ColumnInfo(name = "workListId") val workListId: Int,
    @ColumnInfo(name = "reminderDate") val reminderDate: Long?,
    @ColumnInfo(name = "isDailyReminder") val isDailyReminder: Boolean,
    @ColumnInfo(name = "priority") val priority: String
) : Serializable