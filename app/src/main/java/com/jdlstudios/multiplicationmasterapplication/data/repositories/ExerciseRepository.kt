package com.jdlstudios.multiplicationmasterapplication.data.repositories

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise

interface ExerciseRepository {
    suspend fun saveExercise(exercise: Exercise)
    suspend fun getExercisesBySessionId(sessionId: Long): List<Exercise>
    suspend fun uploadExercise(exercise: Exercise)
    suspend fun getExerciseResult(op1: Int, op2: Int): Int
}