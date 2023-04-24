package com.jdlstudios.multiplicationmasterapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.ExerciseCacheEntity

data class Exercise(
    val id: Long = 0,
    val sessionId: Long,
    val operand1: Int,
    val operand2: Int,
    val answer: Int,
    val correct: Boolean
) {
    fun toExerciseCacheEntity(): ExerciseCacheEntity {
        return ExerciseCacheEntity(
            id = id,
            sessionId = sessionId,
            operand1 = operand1,
            operand2 = operand2,
            answer = answer,
            correct = correct
        )
    }
}