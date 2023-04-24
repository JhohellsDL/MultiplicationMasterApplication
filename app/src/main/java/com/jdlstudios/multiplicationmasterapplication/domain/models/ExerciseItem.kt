package com.jdlstudios.multiplicationmasterapplication.domain.models

import com.jdlstudios.multiplicationmasterapplication.data.cache.models.ExerciseCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise

data class ExerciseItem(
    val id: Long = 0,
    val sessionId: Long,
    val operand1: Int,
    val operand2: Int,
    val answer: Int,
    val correct: Boolean
)

fun ExerciseCacheEntity.toDomain() = ExerciseItem(id, sessionId, operand1, operand2, answer, correct)
fun Exercise.toDomain() = ExerciseItem(id, sessionId, operand1, operand2, answer, correct)