package com.example.taskmanagerapp.utils

import com.example.taskmanagerapp.models.TaskData

interface ItemPosition {
    fun currentTask(task: TaskData)
}