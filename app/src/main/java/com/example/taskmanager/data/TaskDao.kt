package com.example.taskmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.data.models.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // when using flow we need not to write suspend in front of
    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getSelectedTask(taskId: Int): Flow<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(taskEntity: TaskEntity)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query(
        """SELECT * FROM task_table 
        WHERE title LIKE :searchQuery 
        OR description LIKE :searchQuery"""
    )
    fun searchDatabase(searchQuery: String): Flow<List<TaskEntity>>

    @Query(
        """SELECT * FROM task_table 
        ORDER BY CASE 
        WHEN priority LIKE 'L%' THEN 1 
        WHEN priority LIKE 'M%' THEN 2 
        WHEN priority LIKE 'H%' THEN 3 END"""
    )
    fun sortByLowPriority(): Flow<List<TaskEntity>>

    @Query(
        """SELECT * FROM task_table 
        ORDER BY CASE 
        WHEN priority LIKE 'H%' THEN 1 
        WHEN priority LIKE 'M%' THEN 2 
        WHEN priority LIKE 'L%' THEN 3 END"""
    )
    fun sortByHighPriority(): Flow<List<TaskEntity>>

}