package com.example.taskmanagerapp.converters

import androidx.room.TypeConverter

class TypeConverter {

    @TypeConverter
    fun fromTimestamp(value:Long): java.util.Date {
        return java.util.Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date): Long {
        return date.time
    }
}