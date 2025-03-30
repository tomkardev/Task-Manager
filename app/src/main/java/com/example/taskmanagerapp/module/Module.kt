package com.example.taskmanagerapp.module

import android.content.Context
import androidx.room.Room
import com.example.taskmanagerapp.dao.TaskDao
import com.example.taskmanagerapp.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun getRoomBuider(@ApplicationContext context: Context): TaskDatabase{

        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "mvvm_room_diffutil"
        ).build()

    }

    @Provides
    @Singleton
    fun getTasksDao(notesDatabase: TaskDatabase): TaskDao{
        return notesDatabase.taskDao
    }

}