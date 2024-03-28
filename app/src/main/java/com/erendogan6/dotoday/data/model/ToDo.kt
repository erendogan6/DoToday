package com.erendogan6.dotoday.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "ToDo",
    foreignKeys = [
        ForeignKey(
            entity = WorkList::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("workListId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ToDo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean = false,
    @ColumnInfo(name = "dueDate") val dueDate: Long?,
    @ColumnInfo(name = "workListId") val workListId: Int
) : Serializable