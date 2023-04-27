package com.jdlstudios.multiplicationmasterapplication.domain.models

import java.util.Locale

enum class Difficulty {
    EASY,
    INTERMEDIATE,
    CHALLENGING,
    ADVANCED;

    companion object {
        private val values = mapOf(
            EASY to "Easy",
            INTERMEDIATE to "Intermediate",
            CHALLENGING to "Challenging",
            ADVANCED to "Advanced"
        )

        fun getDifficultyFromString(name: String): Difficulty {
            return when (name.uppercase(Locale.ROOT)) {
                "EASY" -> EASY
                "INTERMEDIATE" -> INTERMEDIATE
                "CHALLENGING" -> CHALLENGING
                "ADVANCED" -> ADVANCED
                else -> throw IllegalArgumentException("Invalid difficulty level name")
            }
        }

        fun getDifficultyToString(difficultyLevel: Difficulty): String {
            return values[difficultyLevel]
                ?: throw IllegalArgumentException("Invalid difficulty level")
        }

    }
}
