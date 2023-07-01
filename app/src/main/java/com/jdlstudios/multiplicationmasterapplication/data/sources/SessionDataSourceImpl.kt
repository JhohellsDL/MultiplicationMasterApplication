package com.jdlstudios.multiplicationmasterapplication.data.sources

import com.jdlstudios.multiplicationmasterapplication.data.local.dao.ExerciseDao
import com.jdlstudios.multiplicationmasterapplication.data.local.dao.SessionDao
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.models.Session

class SessionDataSourceImpl(
    private val sessionDao: SessionDao,
    private val exerciseDao: ExerciseDao
) :
    SessionDataSource {
    override suspend fun createSession(session: Session): Long {
        sessionDao.insertSession(session.toSessionEntity())
        return 0
    }

    override suspend fun getSession(sessionId: Long): Session {
        return sessionDao.getSessionBySessionId(sessionId).toSession()
    }

    override suspend fun getSessions(): List<Session> {
        TODO("Not yet implemented")
    }

    override suspend fun saveExercise(sessionId: Long, exercise: Exercise) {
        val exerciseCacheEntity = exercise.toExerciseEntity()
        exerciseDao.insertExercise(exerciseCacheEntity)
    }

    override suspend fun getExercises(sessionId: Long): List<Exercise> {
        return exerciseDao.getExercisesForSession(sessionId).map { it.toExercise() }
    }
}