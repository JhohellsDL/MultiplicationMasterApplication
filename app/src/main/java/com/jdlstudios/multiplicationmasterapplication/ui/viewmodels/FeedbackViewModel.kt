package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import android.util.Log
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
    private val exerciseRepository: ExerciseRepositoryImpl
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

    fun getListExercises(sessionId: Long){
        viewModelScope.launch {
            exerciseRepository.getExercisesBySessionId(sessionId).let {
                _listExercises.value = it
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