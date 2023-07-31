package com.example.taskmanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmanager.utils.Constants.DATABASE_TABLE_NAME

@Entity(tableName = DATABASE_TABLE_NAME)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
)
