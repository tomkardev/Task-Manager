package com.example.taskmanagerapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity (tableName = "taskTable")
data class TaskData(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "taskTitle")
    val title: String,
    @ColumnInfo(name = "taskDescription")
    val description: String? = "",
    @ColumnInfo(name = "taskPriority")
    val priority: Int,
    @ColumnInfo(name = "taskDueDate")
    val dueDate: java.util.Date,
    @ColumnInfo(name = "taskStatus")
    val status: String
): Parcelable
