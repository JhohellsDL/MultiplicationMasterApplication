package com.jdlstudios.multiplicationmasterapplication.data.repositories

import com.jdlstudios.multiplicationmasterapplication.data.models.Session

interface SessionRepository {

    suspend fun saveSession(session: Session)

    suspend fun getSessions(): List<Session>

}