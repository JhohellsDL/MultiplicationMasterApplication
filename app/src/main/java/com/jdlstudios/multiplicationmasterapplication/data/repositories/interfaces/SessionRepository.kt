package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces

import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Session

interface SessionRepository {
    suspend fun saveSession(session: Session)
    suspend fun updateSession(session: Session)
    suspend fun getSessionBySessionId(sessionId: Long): Session

}