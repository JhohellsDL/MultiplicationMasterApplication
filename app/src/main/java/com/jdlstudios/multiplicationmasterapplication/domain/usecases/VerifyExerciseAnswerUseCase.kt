package com.jdlstudios.multiplicationmasterapplication.domain.usecases

import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.ExerciseRepository

class VerifyExerciseAnswerUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(
        operator1: Int,
        operator2: Int,
        userAnswer: Int
    ): Boolean {
        val correctAnswer = exerciseRepository.getExerciseResult(
            operator1,
            operator2
        )
        return userAnswer == correctAnswer
    }
}
