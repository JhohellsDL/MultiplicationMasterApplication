package com.jdlstudios.multiplicationmasterapplication.data.sources

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.models.Session

interface SessionDataSource {
    suspend fun createSession(session: Session): Long
    suspend fun getSession(sessionId: Long): Session?
    suspend fun getSessions(): List<Session>
    suspend fun saveExercise(sessionId: Long, exercise: Exercise)
    suspend fun getExercises(sessionId: Long): List<Exercise>
}