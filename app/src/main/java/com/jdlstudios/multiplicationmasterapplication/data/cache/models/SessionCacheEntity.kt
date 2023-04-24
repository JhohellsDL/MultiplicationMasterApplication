package com.jdlstudios.multiplicationmasterapplication.data.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdlstudios.multiplicationmasterapplication.data.models.Session

@Entity(tableName = "sessions")
data class SessionCacheEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0L,
    val difficulty: Int,
    val numberOfExercises: Int,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
){
    fun toSession(): Session{
        return Session(
            sessionId = sessionId,
            difficulty = difficulty,
            numberOfExercises = numberOfExercises,
            score = score,
            timestamp = timestamp
        )
    }
}