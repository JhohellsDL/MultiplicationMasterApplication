package com.jdlstudios.multiplicationmasterapplication.domain.usecases

import com.jdlstudios.multiplicationmasterapplication.data.repositories.provider.ExerciseProvider
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.models.ExerciseItem
import com.jdlstudios.multiplicationmasterapplication.domain.models.toDomain
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel

class GenerateListExercisesUseCase {

    fun generateListExercises(difficulty: Difficulty, quantity: Int): List<ExerciseItem> {
        val listExercises: MutableList<ExerciseItem> = mutableListOf()
        for (i in 0 until quantity) {
            listExercises.add(ExerciseProvider.randomMultiplication(difficulty).toDomain())
        }
        return listExercises
    }

}
