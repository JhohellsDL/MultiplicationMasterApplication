package com.jdlstudios.multiplicationmasterapplication.domain.models

import com.jdlstudios.multiplicationmasterapplication.data.cache.models.MultiplicationTableCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable

data class MultiplicationTableItem(
    val id: Int,
    val difficulty: Difficulty,
    val totalExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timestamp: Long
)

fun MultiplicationTableItem.toRepository() = MultiplicationTable(
    id,
    difficulty,
    totalExercises,
    correctAnswers,
    incorrectAnswers,
    timestamp
)
fun MultiplicationTableCacheEntity.toDomain() = MultiplicationTableItem(
    id,
    Difficulty.values()[difficulty],
    totalExercises,
    correctAnswers,
    incorrectAnswers,
    timestamp
)
fun MultiplicationTable.toDomain() = MultiplicationTableItem(
    id,
    difficulty,
    totalExercises,
    correctAnswers,
    incorrectAnswers,
    timestamp
)