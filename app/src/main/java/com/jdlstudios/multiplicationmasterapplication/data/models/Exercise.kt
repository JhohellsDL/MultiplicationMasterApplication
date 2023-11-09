package com.jdlstudios.multiplicationmasterapplication.data.models

import com.jdlstudios.multiplicationmasterapplication.data.local.models.ExerciseEntity
import com.jdlstudios.multiplicationmasterapplication.domain.models.ExerciseItem

data class Exercise(
    val sessionId: Long,
    val operand1: Int,
    val operand2: Int,
    val answer: Int,
    val answerUser: Int,
    val correct: Boolean,
    val time: Long = System.currentTimeMillis()
) {
    fun toExerciseEntity(): ExerciseEntity {
        return ExerciseEntity(
            sessionId = sessionId,
            operand1 = operand1,
            operand2 = operand2,
            answer = answer,
            answerUser = answerUser,
            correct = correct,
            time = time
        )
    }
}