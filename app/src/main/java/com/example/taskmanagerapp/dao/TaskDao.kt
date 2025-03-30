package com.example.taskmanagerapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanagerapp.models.TaskData

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskData)

    @Delete
    suspend fun deleteTask(task: TaskData)

    @Update
    suspend fun updateTask(task: TaskData)

    @Query("SELECT * FROM taskTable")
    suspend fun getAllTasks(): List<TaskData>

    @Query("SELECT * FROM taskTable ORDER BY taskPriority ASC")
    suspend fun getTaskByPriority(): List<TaskData>

    @Query("SELECT * FROM taskTable ORDER BY taskTitle DESC")
    suspend fun getTaskAlph(): List<TaskData>

    @Query("SELECT * FROM taskTable ORDER BY taskDueDate DESC")
    suspend fun getTaskDueDate(): List<TaskData>

    @Query("SELECT * FROM taskTable WHERE taskTitle LIKE :query ")
    suspend fun serachTasks(query: String?): List<TaskData>

    @Query("SELECT * FROM taskTable WHERE taskStatus = 'Completed' ")
    suspend fun getCompTask(): List<TaskData>

    @Query("SELECT * FROM taskTable WHERE taskStatus = 'Pending' ")
    suspend fun getPendingTask(): List<TaskData>



}