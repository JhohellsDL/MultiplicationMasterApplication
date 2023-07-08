package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl

import com.jdlstudios.multiplicationmasterapplication.data.local.dao.SessionDao
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.SessionRepository
import kotlinx.coroutines.flow.Flow

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun saveSession(session: Session) {
        sessionDao.insertSession(session.toSessionEntity())
    }

    val allSessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()

    override suspend fun updateSession(session: SessionEntity) {
        sessionDao.updateSession(session)
    }

    override suspend fun getSessionBySessionId(sessionId: Long): Session {
        return sessionDao.getSessionBySessionId(sessionId).toSession()
    }

    override suspend fun deleteAllSessions() {
        sessionDao.deleteAllSessions()
    }

}