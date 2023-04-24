package com.jdlstudios.multiplicationmasterapplication.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.ExerciseCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.MultiplicationTableCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.SessionCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.ExerciseCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.MultiplicationTableCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.SessionCacheEntity

@Database(entities = [MultiplicationTableCacheEntity::class, ExerciseCacheEntity::class, SessionCacheEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun multiplicationTableDao(): MultiplicationTableCacheDao
    abstract fun exerciseDao(): ExerciseCacheDao
    abstract fun sessionDao(): SessionCacheDao

    companion object {
        private const val DATABASE_NAME = "my_app_db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}
