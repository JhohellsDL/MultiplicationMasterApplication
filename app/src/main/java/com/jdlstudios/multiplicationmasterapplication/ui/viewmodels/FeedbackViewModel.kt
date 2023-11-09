package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.ExerciseRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl
import kotlinx.coroutines.launch

class FeedbackViewModel(
    private val sessionRepository: SessionRepositoryImpl,
    private val exerciseRepository: ExerciseRepositoryImpl,
    private val userId: Long
) : ViewModel() {

    private val _currentSession = MutableLiveData<SessionEntity>()
    val currentSession: LiveData<SessionEntity>
        get() = _currentSession


    private val _listExercises = MutableLiveData<List<Exercise>>()
    val listExercises: LiveData<List<Exercise>>
        get() = _listExercises

    init {
        getCurrentSession()
    }

    fun getListExercises(sessionId: Long) {
        viewModelScope.launch {
            exerciseRepository.getExercisesBySessionId(sessionId).let {
                _listExercises.value = it
            }
        }
    }

    fun getListExercises() {
        viewModelScope.launch {
            _currentSession.value?.let { session ->
                exerciseRepository.getExercisesBySessionId(session.sessionId).let {
                    _listExercises.value = it
                }
            }
        }
    }

    fun getCurrentSession(sessionId: Long) {
        viewModelScope.launch {
            sessionRepository.getSessionBySessionId(sessionId).let {
                _currentSession.value = it.toSessionEntity()
            }
        }
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