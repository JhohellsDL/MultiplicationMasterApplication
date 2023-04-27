package com.jdlstudios.multiplicationmasterapplication.domain.usecases

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.repositories.provider.ExerciseProvider
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.models.ExerciseItem
import com.jdlstudios.multiplicationmasterapplication.domain.models.toDomain

class GenerateExerciseUseCase {

    fun generateExercise(difficulty: Difficulty): ExerciseItem {
        return ExerciseProvider.randomMultiplication(difficulty).toDomain()
    }

}