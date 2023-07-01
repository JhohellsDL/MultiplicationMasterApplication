package com.jdlstudios.multiplicationmasterapplication.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdlstudios.multiplicationmasterapplication.data.models.Session

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0L,
    val difficulty: Int,
    val numberOfExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
){
    fun toSession(): Session{
        return Session(
            difficulty = difficulty,
            numberOfExercises = numberOfExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers =incorrectAnswers,
            score = score,
            timestamp = timestamp
        )
    }
}