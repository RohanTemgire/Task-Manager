package com.example.taskmanager.di

import android.app.Application
import androidx.room.Room
import com.example.taskmanager.data.TaskDao
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDb(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(database: TaskDatabase) : TaskDao {
        return database.taskDao()
    }

}