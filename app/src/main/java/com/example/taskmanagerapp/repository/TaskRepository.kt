package com.example.taskmanagerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanagerapp.dao.TaskDao
import com.example.taskmanagerapp.models.TaskData
import com.example.taskmanagerapp.utils.NetworkResult
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    private val _getAllTasks = MutableLiveData<NetworkResult<List<TaskData>>>()
    val getAllTasksLiveData: LiveData<NetworkResult<List<TaskData>>>
        get() = _getAllTasks


    suspend fun getAllTasks() {
        _getAllTasks.postValue(NetworkResult.Loading())
        try {
            val response = taskDao.getAllTasks()
            _getAllTasks.postValue(NetworkResult.Success(response))
        } catch (e: Exception) {
            _getAllTasks.postValue(NetworkResult.Error(e.message))
        }
    }


    suspend fun getTaskByPriority() {
        _getAllTasks.postValue(NetworkResult.Loading())
        try {
            val response = taskDao.getTaskByPriority()
            _getAllTasks.postValue(NetworkResult.Success(response))
        } catch (e: Exception) {
            _getAllTasks.postValue(NetworkResult.Error(e.message))
        }
    }


    suspend fun addTask(taskData: TaskData) {
        taskDao.addTask(taskData)
    }

    suspend fun deleteTask(taskData: TaskData) {
        taskDao.deleteTask(taskData)
    }

    suspend fun getTaskAlph() {
        _getAllTasks.postValue(NetworkResult.Loading())
        try {
            val response = taskDao.getTaskAlph()
            _getAllTasks.postValue(NetworkResult.Success(response))
        } catch (e: Exception) {
            _getAllTasks.postValue(NetworkResult.Error(e.message))
        }
    }

    suspend fun getTaskDue() {
        _getAllTasks.postValue(NetworkResult.Loading())
        try {
            val response = taskDao.getTaskDueDate()
            _getAllTasks.postValue(NetworkResult.Success(response))
        } catch (e: Exception) {
            _getAllTasks.postValue(NetworkResult.Error(e.message))
        }
    }

    suspend fun searchTask(query: String) {
        _getAllTasks.postValue(NetworkResult.Loading())
        try {
            val response = taskDao.serachTasks(query)
            _getAllTasks.postValue(NetworkResult.Success(response))
        } catch (e: Exception) {
            _getAllTasks.postValue(NetworkResult.Error(e.message))
        }
    }


    private val _getComTasks = MutableLiveData<List<TaskData>>()
    val getCompLiveData: LiveData<List<TaskData>>
        get() = _getComTasks

    private val _getPendingTasks = MutableLiveData<List<TaskData>>()
    val getPendLiveData: LiveData<List<TaskData>>
        get() = _getPendingTasks


    suspend fun getCompTasks() {
        _getComTasks.postValue(taskDao.getCompTask())
    }

    suspend fun getPending() {
        _getPendingTasks.postValue(taskDao.getPendingTask())
    }

    suspend fun updateTask(taskData: TaskData) {
        taskDao.updateTask(taskData)
    }


}