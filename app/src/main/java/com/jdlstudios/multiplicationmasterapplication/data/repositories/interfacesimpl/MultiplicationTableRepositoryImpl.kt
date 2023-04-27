package com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl

import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.MultiplicationTableCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.MultiplicationTableRepository
import com.jdlstudios.multiplicationmasterapplication.data.sources.MultiplicationTableDataSource
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class MultiplicationTableRepositoryImpl(
    private val multiplicationTableDao: MultiplicationTableCacheDao,
    private val multiplicationTableDataSource: MultiplicationTableDataSource
) : MultiplicationTableRepository {

    override suspend fun saveMultiplicationTable(multiplicationTable: MultiplicationTable): MultiplicationTable {
        return multiplicationTableDataSource.saveMultiplicationTable(multiplicationTable)
    }

    override suspend fun getMultiplicationTables(): List<MultiplicationTable> {
        return multiplicationTableDataSource.getMultiplicationTables()
    }

    override suspend fun getMultiplicationTablesByDifficulty(difficulty: Difficulty): List<MultiplicationTable> {
        return multiplicationTableDataSource.getMultiplicationTablesByDifficulty(difficulty)
    }

    override suspend fun generateMultiplicationTable(
        difficulty: Difficulty,
        exercises: Int
    ): MultiplicationTable {
        return MultiplicationTable(
            id = 0,
            difficulty = difficulty,
            totalExercises = exercises,
            correctAnswers = 0,
            incorrectAnswers = 0,
            timestamp = 0
        )
    }


}