package com.jdlstudios.multiplicationmasterapplication.data.repositories.provider

import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class ExerciseProvider {

    companion object{

        private val difficultyLevels = mapOf(
            Difficulty.FACIL to Pair(0..9, 0..9),
            Difficulty.INTERMEDIO to Pair(10..99, 1..9),
            Difficulty.DESAFIO to Pair(100..999, 10..99),
            Difficulty.AVANZADO to Pair(100..999, 100..999)
        )

        fun randomMultiplication(level: Difficulty): Exercise {
            val (factorRange1, factorRange2) = difficultyLevels[level]
                ?: throw IllegalArgumentException("Invalid difficulty level")
            val f1 = factorRange1.random()
            val f2 = factorRange2.random()
            val r = f1 * f2

            return Exercise(
                sessionId = 0,
                operand1 = f1,
                operand2 = f2,
                answer = r,
                answerUser = 0,
                correct = false,
                time = 0
            )
        }

    }
}