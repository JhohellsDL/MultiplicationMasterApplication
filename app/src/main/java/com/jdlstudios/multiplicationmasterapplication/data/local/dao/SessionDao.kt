package com.jdlstudios.multiplicationmasterapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SessionDao {

    @Query("SELECT * FROM sessions")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    suspend fun getSessionBySessionId(sessionId: Long): SessionEntity

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

}