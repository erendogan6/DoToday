package com.erendogan6.dotoday.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize @Entity(tableName = "work_list") data class WorkList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, @ColumnInfo(name = "name") var name: String
) : Parcelable
