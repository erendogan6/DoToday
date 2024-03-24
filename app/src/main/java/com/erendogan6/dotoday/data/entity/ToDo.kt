package com.erendogan6.dotoday.data.entity

import java.io.Serializable

data class ToDo(
    val id: Int,
    var title: String,
    var description: String,
    var isCompleted: Boolean = false,
    var dueDate: Long? = null
):Serializable
