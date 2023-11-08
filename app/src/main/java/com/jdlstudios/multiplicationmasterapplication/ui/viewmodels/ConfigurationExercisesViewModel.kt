package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigurationExercisesViewModel(
    private val sessionRepository: SessionRepositoryImpl
) : ViewModel() {

    private var _currentSession = MutableLiveData<Session>()
    val currentSession: LiveData<Session>
        get() = _currentSession

    fun insertSession() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _currentSession.value?.let {
                    sessionRepository.saveSession(it)
                }
            }
        }
    }

    fun sessionForAdd(difficulty: Difficulty, numberOfExercises: Int) {
        _currentSession.value = Session(
            difficulty = difficulty.ordinal,
            numberOfExercises = numberOfExercises,
            correctAnswers = 0,
            incorrectAnswers = 0,
            score = 0
        )
    }

}