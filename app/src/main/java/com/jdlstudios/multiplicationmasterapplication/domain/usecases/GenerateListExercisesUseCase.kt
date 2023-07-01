package com.jdlstudios.multiplicationmasterapplication.domain.usecases

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.repositories.provider.ExerciseProvider
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.models.ExerciseItem
import com.jdlstudios.multiplicationmasterapplication.domain.models.toDomain
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel

class GenerateListExercisesUseCase {

    fun generateListExercises(difficulty: Difficulty, quantity: Int): List<Exercise> {
        val listExercises: MutableList<Exercise> = mutableListOf()
        for (i in 0 until quantity) {
            listExercises.add(ExerciseProvider.randomMultiplication(difficulty))
        }
        return listExercises
    }

}
