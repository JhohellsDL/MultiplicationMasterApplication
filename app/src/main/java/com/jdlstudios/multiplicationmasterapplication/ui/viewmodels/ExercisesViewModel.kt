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

    private val _exercise = MutableLiveData<ExerciseUIModel>()
    val exercise: LiveData<ExerciseUIModel>
        get() = _exercise

    private val _totalExercises = MutableLiveData<Int>()
    val totalExercises: LiveData<Int>
        get() = _totalExercises

    private var remainingExercises = 0

    private val _listExercises = MutableLiveData<List<ExerciseUIModel>>()
    val listExercises: LiveData<List<ExerciseUIModel>>
        get() = _listExercises

    private val generateExerciseUseCase: GenerateExerciseUseCase = GenerateExerciseUseCase()
    private val generateListExercisesUseCase: GenerateListExercisesUseCase =
        GenerateListExercisesUseCase()

    //----------------------------------------------------------------------------------------------
    fun setExercise(difficulty: Difficulty, quantity: Int) {
        val exercise = generateExerciseUseCase.generateExercise(difficulty)
        _exercise.value = exercise.toUIModel()
        //totalExercises = quantity
    }

    fun setListExercises(difficulty: Difficulty, quantity: Int) {
        val list: List<ExerciseUIModel> =
            generateListExercisesUseCase.generateListExercises(difficulty, quantity).map {
                it.toUIModel()
            }
        _listExercises.value = list
        _totalExercises.value = list.size
    }

    fun setValueCorrect(value: Boolean): ExerciseUIModel {
        val exercise = _exercise.value
        exercise?.correct = value
        return exercise!!
    }

    fun checkAnswer(answer: Int): Boolean {
        val answerExercise = exercise.value?.answer
        Log.i("sum", "$answerExercise = $answer")
        return if (answer == answerExercise) {
            _exercise.value?.correct = true
            true
        } else {
            false
        }
    }

    fun getResultExercise(): ExerciseUIModel? {
        return exercise.value
    }




    fun completeExercise() {
        remainingExercises--
    }

    fun getRemainingExercises(): Int {
        return remainingExercises
    }


}