package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl

import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.SessionRepository
import com.jdlstudios.multiplicationmasterapplication.data.sources.SessionDataSource

class SessionRepositoryImpl(
    private val sessionDataSource: SessionDataSource
) : SessionRepository {

    override suspend fun saveSession(session: Session) {
        sessionDataSource.createSession(session)
    }

    override suspend fun getSessions(): List<Session> {
        return sessionDataSource.getSessions()
    }

}