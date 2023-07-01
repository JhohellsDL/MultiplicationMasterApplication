package com.jdlstudios.multiplicationmasterapplication.data.models

import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity

data class Session(
    val difficulty: Int,
    val numberOfExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
){
    fun toSessionEntity(): SessionEntity {
        return SessionEntity(
            difficulty = difficulty,
            numberOfExercises = numberOfExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            score = score,
            timestamp = timestamp
        )
    }
}