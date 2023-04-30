package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.usecases.GenerateExerciseUseCase
import com.jdlstudios.multiplicationmasterapplication.domain.usecases.GenerateListExercisesUseCase
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel
import com.jdlstudios.multiplicationmasterapplication.ui.models.toUIModel

class ExercisesViewModel : ViewModel() {

    private val _exerciseNew = MutableLiveData<ExerciseUIModel>()
    val exerciseNew: LiveData<ExerciseUIModel>
        get() = _exerciseNew

    private val _stateFinish = MutableLiveData<Boolean>()
    val stateFinish: LiveData<Boolean>
        get() = _stateFinish

    private val _remainingExercises = MutableLiveData<Int>()
    val remainingExercises: LiveData<Int>
        get() = _remainingExercises

    private val _listExercises = MutableLiveData<List<ExerciseUIModel>>()
    val listExercises: LiveData<List<ExerciseUIModel>>
        get() = _listExercises

    private val _selectedPosition = MutableLiveData<Int>()
    val selectedPosition: LiveData<Int> = _selectedPosition

    private val generateListExercisesUseCase: GenerateListExercisesUseCase =
        GenerateListExercisesUseCase()

    fun setListExercises(difficulty: Difficulty, quantity: Int) {
        val list: List<ExerciseUIModel> =
            generateListExercisesUseCase.generateListExercises(difficulty, quantity).map {
                it.toUIModel()
            }
        _listExercises.value = list
        _remainingExercises.value = list.size
        _stateFinish.value = false
        _selectedPosition.value = 0 // Inicialmente seleccionamos el primer elemento
    }

    fun nextItem() {
        val currentPos = _selectedPosition.value ?: 0
        if (currentPos < (listExercises.value?.size?.minus(1) ?: 0)) {
            _selectedPosition.value = currentPos + 1
        }
    }

    fun checkAnswer(answer: Int, exercise: ExerciseUIModel): Boolean {
        val answerExercise = exercise.answer
        return answer == answerExercise
    }

    fun setExerciseNew(exercise: ExerciseUIModel) {
        _exerciseNew.value = exercise
    }

    fun completeExercise() {
        _remainingExercises.value = _remainingExercises.value!! - 1
        if (_remainingExercises.value == 0) {
            _stateFinish.value = true
        }
    }

    fun getRemainingExercises(): Int? {
        return remainingExercises.value
    }

}