package com.jdlstudios.multiplicationmasterapplication.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.ExerciseCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.SessionCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface  SessionCacheDao {

    @Query("SELECT * FROM sessions")
    fun getAllSessions(): List<SessionCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionCacheEntity)

    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    suspend fun getSessionBySessionId(sessionId: Long): SessionCacheEntity

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

}