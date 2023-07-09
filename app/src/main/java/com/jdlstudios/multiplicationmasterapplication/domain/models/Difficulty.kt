package com.jdlstudios.multiplicationmasterapplication.domain.models

import java.util.Locale

enum class Difficulty {
    FACIL,
    INTERMEDIO,
    DESAFIO,
    AVANZADO;

    companion object {
        private val values = mapOf(
            FACIL to "Facil",
            INTERMEDIO to "Intermedio",
            DESAFIO to "Desafio",
            AVANZADO to "Avanzado"
        )
        fun getDifficultyFromString(name: String): Difficulty {
            return when (name.uppercase(Locale.ROOT)) {
                "FACIL" -> FACIL
                "INTERMEDIO" -> INTERMEDIO
                "DESAFIO" -> DESAFIO
                "AVANZADO" -> AVANZADO
                else -> throw IllegalArgumentException("Invalid difficulty level name")
            }
        }

        fun getDifficultyFromInt(value: Int): Difficulty {
            return when (value) {
                0 -> FACIL
                1 -> INTERMEDIO
                2 -> DESAFIO
                3 -> AVANZADO
                else -> throw IllegalArgumentException("Invalid difficulty level value")
            }
        }
        fun getDifficultyToString(difficultyLevel: Difficulty): String {
            return values[difficultyLevel]
                ?: throw IllegalArgumentException("Invalid difficulty level")
        }

    }
}
