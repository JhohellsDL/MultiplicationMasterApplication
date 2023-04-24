package com.jdlstudios.multiplicationmasterapplication.data.sources

import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.ExerciseCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.SessionCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.SessionCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import kotlinx.coroutines.flow.map

class SessionDataSourceImpl(
    private val sessionDao: SessionCacheDao,
    private val exerciseDao: ExerciseCacheDao
) :
    SessionDataSource {
    override suspend fun createSession(session: Session): Long {
        sessionDao.insertSession(session.toSessionCacheEntity())
        return session.sessionId
    }

    override suspend fun getSession(sessionId: Long): Session {
        return sessionDao.getSessionBySessionId(sessionId).toSession()
    }

    override suspend fun getSessions(): List<Session> {
        return sessionDao.getAllSessions().map { it.toSession() }
    }

    override suspend fun saveExercise(sessionId: Long, exercise: Exercise) {
        val exerciseCacheEntity = exercise.toExerciseCacheEntity()
        exerciseDao.insertExercise(exerciseCacheEntity)
    }

    override suspend fun getExercises(sessionId: Long): List<Exercise> {
        return exerciseDao.getExercisesForSession(sessionId).map { it.toExercise() }
    }
}