package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.ExerciseRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl

class FeedbackViewModelFactory(
    private val sessionRepository: SessionRepositoryImpl,
    private val exerciseRepository: ExerciseRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
            return FeedbackViewModel(sessionRepository, exerciseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}