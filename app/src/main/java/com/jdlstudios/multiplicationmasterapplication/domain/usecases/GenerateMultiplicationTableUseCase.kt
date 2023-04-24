package com.jdlstudios.multiplicationmasterapplication.domain.usecases

import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.data.repositories.MultiplicationTableRepository
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class GenerateMultiplicationTableUseCase(private val repository: MultiplicationTableRepository) {

    sealed class Result {
        data class Success(val multiplicationTable: MultiplicationTable) : Result()
        data class Error(val exception: Exception) : Result()
    }

    suspend operator fun invoke(difficulty: Int, exercises: Int): Result {
        return try {
            val multiplicationTable = repository.generateMultiplicationTable(
                Difficulty.values()[difficulty],
                exercises
            )
            Result.Success(multiplicationTable)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
