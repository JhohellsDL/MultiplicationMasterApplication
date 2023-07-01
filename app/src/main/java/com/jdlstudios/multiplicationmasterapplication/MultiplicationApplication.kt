package com.jdlstudios.multiplicationmasterapplication

import android.app.Application
import com.jdlstudios.multiplicationmasterapplication.data.local.AppDatabase
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.ExerciseRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl

class MultiplicationApplication : Application() {

    private val database by lazy {
        AppDatabase.getInstance(this)
    }

    val sessionRepository by lazy {
        SessionRepositoryImpl(database.sessionDao())
    }

    val exerciseRepository by lazy {
        ExerciseRepositoryImpl(database.exerciseDao())
    }
}