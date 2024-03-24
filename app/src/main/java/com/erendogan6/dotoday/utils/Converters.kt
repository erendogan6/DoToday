package com.erendogan6.dotoday.utils
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromBoolean(value: Boolean): Int {
        return if (value) 1 else 0
    }

    @TypeConverter
    fun toBoolean(value: Int): Boolean {
        return value != 0
    }
}
