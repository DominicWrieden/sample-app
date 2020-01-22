package com.dominicwrieden.sampleapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dominicwrieden.sampleapp.data.entity.Person
import com.dominicwrieden.sampleapp.data.entity.Post

@Database(entities = [Person::class, Post::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPersonDao(): PersonDao

    abstract fun getPostDao(): PostDao


    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance
                ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, "sample-app-db")
                    .build()
                    .also { instance = it }
            }
        }
    }
}