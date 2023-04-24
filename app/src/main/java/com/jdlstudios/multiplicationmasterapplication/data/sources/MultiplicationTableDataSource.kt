package com.jdlstudios.multiplicationmasterapplication.data.sources

import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

interface MultiplicationTableDataSource {
    suspend fun saveMultiplicationTable(multiplicationTable: MultiplicationTable): MultiplicationTable
    suspend fun getMultiplicationTables(): List<MultiplicationTable>
    suspend fun getMultiplicationTablesByDifficulty(difficulty: Difficulty): List<MultiplicationTable>
}
