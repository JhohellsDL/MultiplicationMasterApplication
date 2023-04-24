package com.jdlstudios.multiplicationmasterapplication.data.sources

import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.ExerciseCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.ExerciseCacheEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise

class ExerciseDataSource(
    private val exerciseDao: ExerciseCacheDao
) {

    suspend fun saveExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise = exercise.toExerciseCacheEntity())
    }

    suspend fun getExercisesBySessionId(sessionId: Long): List<Exercise> {
        return exerciseDao.getExercisesForSession(sessionId).map { it.toExercise() }
    }

    suspend fun uploadExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise.toExerciseCacheEntity())
    }

}
