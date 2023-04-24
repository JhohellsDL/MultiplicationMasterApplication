package com.jdlstudios.multiplicationmasterapplication.data.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

@Entity(tableName = "multiplication_table")
data class MultiplicationTableCacheEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val difficulty: Int,
    val totalExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timestamp: Long
) {
    fun toMultiplicationTable(): MultiplicationTable {
        return MultiplicationTable(
            id = id,
            difficulty = Difficulty.values()[difficulty],
            totalExercises = totalExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            timestamp = timestamp
        )
    }
}
