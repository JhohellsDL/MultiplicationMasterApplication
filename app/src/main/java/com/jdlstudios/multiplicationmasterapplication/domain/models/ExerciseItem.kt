package com.jdlstudios.multiplicationmasterapplication.domain.models

import com.jdlstudios.multiplicationmasterapplication.data.local.models.ExerciseEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel

data class ExerciseItem(
    val sessionId: Long,
    val operand1: Int,
    val operand2: Int,
    val answer: Int,
    val answerUser: Int,
    val correct: Boolean
)

fun ExerciseEntity.toDomain() =
    ExerciseItem(
        sessionId,
        operand1,
        operand2,
        answer,
        answerUser,
        correct
    )

fun Exercise.toDomain() =
    ExerciseItem(
        sessionId,
        operand1,
        operand2,
        answer,
        answerUser,
        correct
    )

fun ExerciseUIModel.toDomain() =
    ExerciseItem(
        sessionId = 0,
        operand1 = factor1,
        operand2 = factor2,
        answer,
        answerUser,
        correct
    )