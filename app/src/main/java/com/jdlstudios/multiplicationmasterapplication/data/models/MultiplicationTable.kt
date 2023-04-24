package com.jdlstudios.multiplicationmasterapplication.data.models

import com.jdlstudios.multiplicationmasterapplication.data.cache.models.MultiplicationTableCacheEntity
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

data class MultiplicationTable(
    val id: Int,
    val difficulty: Difficulty,
    val totalExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timestamp: Long
){
    fun toMultiplicationTableCacheEntity(): MultiplicationTableCacheEntity {
        return MultiplicationTableCacheEntity(
            difficulty = difficulty.ordinal,
            totalExercises = totalExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            timestamp = timestamp
        )
    }

}
