package com.jdlstudios.multiplicationmasterapplication

import android.app.Application
import com.jdlstudios.multiplicationmasterapplication.data.local.AppDatabase
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl

class MultiplicationApplication : Application() {

    private val database by lazy {
        AppDatabase.getInstance(this)
    }

    val repository by lazy {
        SessionRepositoryImpl(database.sessionDao())
    }
}