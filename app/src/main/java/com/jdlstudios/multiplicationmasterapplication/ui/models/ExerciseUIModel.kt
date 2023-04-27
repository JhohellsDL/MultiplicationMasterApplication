package com.jdlstudios.multiplicationmasterapplication.ui.models

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.domain.models.ExerciseItem

data class ExerciseUIModel(
    val factor1: Int,
    val factor2: Int,
    val answer: Int,
    var correct: Boolean
)

fun Exercise.toUIModel() = ExerciseUIModel(
    factor1 = operand1,
    factor2 = operand2,
    answer = answer,
    correct = correct
)
fun ExerciseItem.toUIModel() = ExerciseUIModel(
    factor1 = operand1,
    factor2 = operand2,
    answer = answer,
    correct = correct
)