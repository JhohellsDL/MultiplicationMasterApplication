package com.jdlstudios.multiplicationmasterapplication.data.sources

import com.jdlstudios.multiplicationmasterapplication.data.cache.dao.MultiplicationTableCacheDao
import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class MultiplicationTableDataSourceImpl(
    private val multiplicationTableDao: MultiplicationTableCacheDao
) : MultiplicationTableDataSource {

    override suspend fun saveMultiplicationTable(multiplicationTable: MultiplicationTable): MultiplicationTable {
        val id =
            multiplicationTableDao.insertMultiplicationTable(multiplicationTable.toMultiplicationTableCacheEntity())
        return multiplicationTable.copy(id = id.toString().toInt())
    }

    override suspend fun getMultiplicationTables(): List<MultiplicationTable> {
        val multiplicationTableCacheEntities = multiplicationTableDao.getAllMultiplicationTables()
        return multiplicationTableCacheEntities?.map { it.toMultiplicationTable() } ?: listOf()
    }

    override suspend fun getMultiplicationTablesByDifficulty(difficulty: Difficulty): List<MultiplicationTable> {
        val multiplicationTableByDifficulty =
            multiplicationTableDao.getMultiplicationTablesByDifficulty(difficulty)
        return multiplicationTableByDifficulty?.map { it.toMultiplicationTable() } ?: listOf()
    }

}

