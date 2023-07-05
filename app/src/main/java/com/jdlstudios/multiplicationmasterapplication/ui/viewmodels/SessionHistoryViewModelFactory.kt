package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl

class SessionHistoryViewModelFactory(
    private val sessionRepositoryImpl: SessionRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionHistoryViewModel::class.java)) {
            return SessionHistoryViewModel(sessionRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}