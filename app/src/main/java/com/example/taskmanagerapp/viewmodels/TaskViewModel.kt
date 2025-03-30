package com.example.taskmanagerapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagerapp.models.TaskData
import com.example.taskmanagerapp.repository.TaskRepository
import com.example.taskmanagerapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    val getAllTaskLiveData: LiveData<NetworkResult<List<TaskData>>>
        get() = repository.getAllTasksLiveData

    fun addTask(task: TaskData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(taskData = task)
        }

    }


    fun deleteTask(task: TaskData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }

    }

    fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTasks()
        }
    }

    fun getTaskByPriority() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTaskByPriority()
        }
    }

    fun getTaskAplh() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTaskAlph()
        }
    }

    fun getTaskByDue() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTaskDue()
        }
    }

    fun searchTask(query: String) {
        viewModelScope.launch {
            repository.searchTask(query)
        }
    }

    val getPendLiveData: LiveData<List<TaskData>>
        get() = repository.getPendLiveData

    val getCompLiveData: LiveData<List<TaskData>>
        get() = repository.getCompLiveData




    fun getCompTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCompTasks()
        }
    }

    fun getPendTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPending()
        }
    }

    fun updateTask(task: TaskData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }
}