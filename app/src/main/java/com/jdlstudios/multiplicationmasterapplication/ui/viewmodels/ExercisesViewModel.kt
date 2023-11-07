package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.ExerciseRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.usecases.GenerateListExercisesUseCase
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExercisesViewModel(
    private val sessionRepository: SessionRepositoryImpl,
    private val exerciseRepository: ExerciseRepositoryImpl
) : ViewModel() {

    private val generateListExercisesUseCase: GenerateListExercisesUseCase =
        GenerateListExercisesUseCase()

    private val _listExercises = MutableLiveData<List<Exercise>>()
    val listExercises: LiveData<List<Exercise>>
        get() = _listExercises

    private val _currentExercise = MutableLiveData<Exercise>()
    val currentExercise: LiveData<Exercise>
        get() = _currentExercise

    private var _currentSession = MutableLiveData<SessionEntity>()
    val currentSession: LiveData<SessionEntity>
        get() = _currentSession

    private var _updateSession = MutableLiveData<SessionEntity>()
    val updateSession: LiveData<SessionEntity>
        get() = _updateSession

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

    fun insertExercise() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _currentExercise.value?.let {
                    Log.i("asd","Exercise Saved!! - exercise: $it")
                    exerciseRepository.saveExercise(it)
                }
            }
        }
    }
    fun updateSession() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _updateSession.value?.let {
                    Log.i("asd","Session Updated!! - session: $it")
                    sessionRepository.updateSession(it)
                }
            }
        }
    }

    fun sessionForUpdate(correctAnswers: Int, incorrectAnswers: Int, score: Int){
        _updateSession.value = SessionEntity(
            sessionId = _currentSession.value!!.sessionId,
            difficulty = _currentSession.value!!.difficulty,
            numberOfExercises = _currentSession.value!!.numberOfExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            score = score
        )
    }

    fun exerciseForAdd2(exercise: Exercise) {
        _currentExercise.value = Exercise(
            sessionId = exercise.sessionId,
            operand1 = exercise.operand1,
            operand2 = exercise.operand2,
            answer = exercise.answer,
            answerUser = exercise.answerUser,
            correct = exercise.correct,
            time = exercise.time
        )
    }

    fun exerciseForAdd(
        sessionId: Long,
        operand1: Int,
        operand2: Int,
        answer: Int,
        answerUser: Int,
        correct: Boolean,
        time: Long
    ) {
        _currentExercise.value = Exercise(
            sessionId = sessionId,
            operand1 = operand1,
            operand2 = operand2,
            answer = answer,
            answerUser = answerUser,
            correct = correct,
            time = time
        )
    }

    // Genera una lista de ejercicios y la setea en _listExercises
    fun setListExercises(difficulty: Difficulty, quantity: Int) {
        val list: List<Exercise> =
            generateListExercisesUseCase.generateListExercises(difficulty, quantity)
        _listExercises.value = list
    }


    // Comprueba si la respuesta es correcta
    fun checkAnswer(answer: Int, exercise: ExerciseUIModel): Boolean {
        val answerExercise = exercise.answer
        return answer == answerExercise
    }


}