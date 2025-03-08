package com.example.taskmanager.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.taskmanager.dB.TaskManagerDB
import com.example.taskmanager.data.local.RoomDao
import com.example.taskmanager.utils.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskManagerDB =
        Room.databaseBuilder(app, TaskManagerDB::class.java, "my_database").build()

    @Provides
    fun provideMyDao(database: TaskManagerDB): RoomDao = database.myDao()

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
        @Provides
        @Singleton
        fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
            return PreferencesManager(context)
        }
    }
}