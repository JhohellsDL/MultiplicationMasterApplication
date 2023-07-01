package com.jdlstudios.multiplicationmasterapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdlstudios.multiplicationmasterapplication.data.local.models.ExerciseEntity

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Query("SELECT * FROM exercise WHERE session_id = :sessionId")
    suspend fun getExercisesForSession(sessionId: Long): List<ExerciseEntity>

    @Query("SELECT COUNT(*) FROM exercise WHERE session_id = :sessionId AND correct = 1")
    suspend fun getCorrectCountForSession(sessionId: Long): Int

    @Query("SELECT COUNT(*) FROM exercise WHERE session_id = :sessionId AND correct = 0")
    suspend fun getIncorrectCountForSession(sessionId: Long): Int

    @Query("DELETE FROM exercise WHERE session_id = :sessionId")
    suspend fun deleteExercisesForSession(sessionId: Long)

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

}