package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.ExerciseRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl
import kotlinx.coroutines.launch

class FeedbackViewModel(
    private val sessionRepository: SessionRepositoryImpl,
    private val exerciseRepository: ExerciseRepositoryImpl
) : ViewModel() {

    private val _currentSession = MutableLiveData<SessionEntity>()
    val currentSession: LiveData<SessionEntity>
        get() = _currentSession

    init {
        getCurrentSession()
    }

    private fun getCurrentSession() {
        viewModelScope.launch {
            sessionRepository.allSessions.let { listFlow ->
                listFlow.collect {
                    _currentSession.value = it.lastOrNull()
                }
            }
        }
    }

}