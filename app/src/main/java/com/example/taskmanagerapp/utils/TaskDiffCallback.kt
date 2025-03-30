package com.example.taskmanagerapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.taskmanagerapp.models.TaskData

class TaskDiffCallbackDiffUtil: DiffUtil.ItemCallback<TaskData>() {
    override fun areItemsTheSame(oldItem: TaskData, newItem: TaskData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskData, newItem: TaskData): Boolean {
        return oldItem == newItem
    }
}