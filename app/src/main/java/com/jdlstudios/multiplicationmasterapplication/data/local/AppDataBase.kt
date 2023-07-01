package com.jdlstudios.multiplicationmasterapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jdlstudios.multiplicationmasterapplication.data.local.dao.ExerciseDao
import com.jdlstudios.multiplicationmasterapplication.data.local.dao.SessionDao
import com.jdlstudios.multiplicationmasterapplication.data.local.models.ExerciseEntity
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity

@Database(entities = [ExerciseEntity::class, SessionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun sessionDao(): SessionDao

    companion object {

        private const val DATABASE_NAME = "my_app_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
