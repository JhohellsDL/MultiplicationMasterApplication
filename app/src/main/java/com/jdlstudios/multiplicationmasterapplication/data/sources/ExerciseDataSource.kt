package com.jdlstudios.multiplicationmasterapplication.data.sources

import com.jdlstudios.multiplicationmasterapplication.data.local.dao.ExerciseDao
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise

class ExerciseDataSource(
    private val exerciseDao: ExerciseDao
) {

    suspend fun saveExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise = exercise.toExerciseEntity())
    }

    suspend fun getExercisesBySessionId(sessionId: Long): List<Exercise> {
        return exerciseDao.getExercisesForSession(sessionId).map { it.toExercise() }
    }

    suspend fun uploadExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise.toExerciseEntity())
    }

}
