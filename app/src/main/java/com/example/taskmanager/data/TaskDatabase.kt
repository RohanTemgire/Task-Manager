package com.example.taskmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.data.models.TaskEntity

@Database(
    entities = [TaskEntity::class],
    exportSchema = false,
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}