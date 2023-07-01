package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl

import com.jdlstudios.multiplicationmasterapplication.data.local.dao.ExerciseDao
import com.jdlstudios.multiplicationmasterapplication.data.local.dao.SessionDao
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.ExerciseRepository
import com.jdlstudios.multiplicationmasterapplication.data.repositories.provider.ExerciseProvider
import com.jdlstudios.multiplicationmasterapplication.data.sources.ExerciseDataSource
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class ExerciseRepositoryImpl(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {

    override suspend fun saveExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise = exercise.toExerciseEntity())
    }

    override suspend fun getExercisesBySessionId(sessionId: Long): List<Exercise> {
        return exerciseDao.getExercisesForSession(sessionId).map {
            it.toExercise()
        }
    }

    override suspend fun uploadExercise(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun getExerciseResult(op1: Int, op2: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getExerciseFromProvider(difficulty: Difficulty): Exercise {
        TODO("Not yet implemented")
    }

}