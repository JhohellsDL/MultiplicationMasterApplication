package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl

import com.jdlstudios.multiplicationmasterapplication.data.local.dao.ExerciseDao
import com.jdlstudios.multiplicationmasterapplication.data.local.dao.SessionDao
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.SessionRepository
import com.jdlstudios.multiplicationmasterapplication.data.sources.SessionDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun saveSession(session: Session) {
        sessionDao.insertSession(session.toSessionEntity())
    }

    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()

    override suspend fun updateSession(session: Session) {
        sessionDao.updateSession(session.toSessionEntity())
    }

    override suspend fun getSessionBySessionId(sessionId: Long): Session {
        return sessionDao.getSessionBySessionId(sessionId).toSession()
    }

}