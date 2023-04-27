package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

interface ExerciseRepository {
    suspend fun saveExercise(exercise: Exercise)
    suspend fun getExercisesBySessionId(sessionId: Long): List<Exercise>
    suspend fun uploadExercise(exercise: Exercise)
    suspend fun getExerciseResult(op1: Int, op2: Int): Int
    fun getExerciseFromProvider(difficulty: Difficulty): Exercise
}