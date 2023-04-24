package com.jdlstudios.multiplicationmasterapplication.data.repositories

import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

interface MultiplicationTableRepository {
    suspend fun saveMultiplicationTable(multiplicationTable: MultiplicationTable): MultiplicationTable
    suspend fun getMultiplicationTables(): List<MultiplicationTable>
    suspend fun getMultiplicationTablesByDifficulty(difficulty: Difficulty): List<MultiplicationTable>
    suspend fun generateMultiplicationTable(difficulty: Difficulty, exercises: Int): MultiplicationTable
}