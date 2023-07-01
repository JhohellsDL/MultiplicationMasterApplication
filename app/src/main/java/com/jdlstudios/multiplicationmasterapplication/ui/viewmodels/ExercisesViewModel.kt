package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdlstudios.multiplicationmasterapplication.data.local.dao.ExerciseDao
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.data.models.toRepository
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.ExerciseRepository
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.ExerciseRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.models.toDomain
import com.jdlstudios.multiplicationmasterapplication.domain.usecases.GenerateListExercisesUseCase
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel
import com.jdlstudios.multiplicationmasterapplication.ui.models.toUIModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExercisesViewModel(
    private val sessionRepository: SessionRepositoryImpl,
    private val exerciseRepository: ExerciseRepositoryImpl
) : ViewModel() {

    private val dao: ExerciseDao? = null
    private val generateListExercisesUseCase: GenerateListExercisesUseCase =
        GenerateListExercisesUseCase()

    // LiveData para la nueva ejercitación generada
    private val _exerciseNew = MutableLiveData<ExerciseUIModel>()
    val exerciseNew: LiveData<ExerciseUIModel>
        get() = _exerciseNew

    // LiveData para indicar si se completó la ejercitación
    private val _stateFinish = MutableLiveData<Boolean>()
    val stateFinish: LiveData<Boolean>
        get() = _stateFinish

    // LiveData para la cantidad de ejercicios restantes
    private val _remainingExercises = MutableLiveData<Int>()
    private val remainingExercises: LiveData<Int>
        get() = _remainingExercises

    // LiveData para la lista de ejercicios generada
    private val _listExercises = MutableLiveData<List<Exercise>>()
    val listExercises: LiveData<List<Exercise>>
        get() = _listExercises

    // Ejercicio para agregar
    private val _currentExercise = MutableLiveData<Exercise>()
    val currentExercise: LiveData<Exercise>
        get() = _currentExercise

    // LiveData para la posición seleccionada actualmente
    private val _selectedPosition = MutableLiveData<Int>()
    val selectedPosition: LiveData<Int> = _selectedPosition


    private var _currentSession = MutableLiveData<SessionEntity>()
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

    fun exerciseForAdd2(exercise: Exercise) {
        _currentExercise.value = Exercise(
            sessionId = exercise.sessionId,
            operand1 = exercise.operand1,
            operand2 = exercise.operand2,
            answer = exercise.answer,
            answerUser = exercise.answerUser,
            correct = exercise.correct
        )
    }

    fun exerciseForAdd(
        sessionId: Long,
        operand1: Int,
        operand2: Int,
        answer: Int,
        answerUser: Int,
        correct: Boolean
    ) {
        _currentExercise.value = Exercise(
            sessionId = sessionId,
            operand1 = operand1,
            operand2 = operand2,
            answer = answer,
            answerUser = answerUser,
            correct = correct
        )
    }

    // Genera una lista de ejercicios y la setea en _listExercises
    fun setListExercises(difficulty: Difficulty, quantity: Int) {
        val list: List<Exercise> =
            generateListExercisesUseCase.generateListExercises(difficulty, quantity)
        _listExercises.value = list
        _remainingExercises.value = list.size
        _stateFinish.value = false
        _selectedPosition.value = 0
    }

    // Avanza a la siguiente posición
    fun nextItem() {
        val currentPos = _selectedPosition.value ?: 0
        if (currentPos < (listExercises.value?.size?.minus(1) ?: 0)) {
            _selectedPosition.value = currentPos + 1
        }
    }

    // Comprueba si la respuesta es correcta
    fun checkAnswer(answer: Int, exercise: ExerciseUIModel): Boolean {
        val answerExercise = exercise.answer
        return answer == answerExercise
    }

    // Setea el ejercicio actual
    fun setExerciseNew(exercise: ExerciseUIModel) {
        _exerciseNew.value = exercise
    }

    // Completa un ejercicio
    fun completeExercise() {
        _remainingExercises.value = _remainingExercises.value!! - 1
        if (_remainingExercises.value == 0) {
            _stateFinish.value = true
        }
    }

    // Retorna la cantidad de ejercicios restantes
    fun getRemainingExercises(): Int? {
        return remainingExercises.value
    }

}