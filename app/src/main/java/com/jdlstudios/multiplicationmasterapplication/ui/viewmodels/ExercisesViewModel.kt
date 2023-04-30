package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.usecases.GenerateListExercisesUseCase
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel
import com.jdlstudios.multiplicationmasterapplication.ui.models.toUIModel

class ExercisesViewModel : ViewModel() {

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
    private val _listExercises = MutableLiveData<List<ExerciseUIModel>>()
    val listExercises: LiveData<List<ExerciseUIModel>>
        get() = _listExercises

    // LiveData para la posición seleccionada actualmente
    private val _selectedPosition = MutableLiveData<Int>()
    val selectedPosition: LiveData<Int> = _selectedPosition

    // Genera una lista de ejercicios y la setea en _listExercises
    fun setListExercises(difficulty: Difficulty, quantity: Int) {
        val list: List<ExerciseUIModel> =
            generateListExercisesUseCase.generateListExercises(difficulty, quantity).map {
                it.toUIModel()
            }
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