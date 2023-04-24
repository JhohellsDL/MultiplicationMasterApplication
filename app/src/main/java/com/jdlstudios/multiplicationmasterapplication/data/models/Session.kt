package com.jdlstudios.multiplicationmasterapplication.data.models

import androidx.room.PrimaryKey
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.MultiplicationTableCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.SessionCacheEntity

data class Session(
    val sessionId: Long = 0L,
    val difficulty: Int,
    val numberOfExercises: Int,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
){
    fun toSessionCacheEntity(): SessionCacheEntity {
        return SessionCacheEntity(
            sessionId = sessionId,
            difficulty = difficulty,
            numberOfExercises = numberOfExercises,
            score = score,
            timestamp = timestamp
        )
    }
}