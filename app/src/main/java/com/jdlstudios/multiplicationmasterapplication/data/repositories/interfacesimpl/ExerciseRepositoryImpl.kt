package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.ExerciseRepository
import com.jdlstudios.multiplicationmasterapplication.data.repositories.provider.ExerciseProvider
import com.jdlstudios.multiplicationmasterapplication.data.sources.ExerciseDataSource
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class ExerciseRepositoryImpl(
    private val exerciseDataSource: ExerciseDataSource
) : ExerciseRepository {

    override suspend fun saveExercise(exercise: Exercise) {
        exerciseDataSource.saveExercise(exercise)
    }

    override suspend fun getExercisesBySessionId(sessionId: Long): List<Exercise> {
        return exerciseDataSource.getExercisesBySessionId(sessionId)
    }

    override suspend fun uploadExercise(exercise: Exercise) {
        return exerciseDataSource.uploadExercise(exercise)
    }

    override suspend fun getExerciseResult(op1: Int, op2: Int): Int {
        return op1 * op2
    }

    override fun getExerciseFromProvider(difficulty: Difficulty): Exercise =
        ExerciseProvider.randomMultiplication(difficulty)
}