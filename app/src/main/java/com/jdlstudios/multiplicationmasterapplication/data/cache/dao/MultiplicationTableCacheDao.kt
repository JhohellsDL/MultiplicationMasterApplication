package com.jdlstudios.multiplicationmasterapplication.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdlstudios.multiplicationmasterapplication.data.cache.models.MultiplicationTableCacheEntity
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

@Dao
interface MultiplicationTableCacheDao {

    @Query("SELECT * FROM multiplication_table WHERE id = :id")
    suspend fun getMultiplicationTableById(id: Int): MultiplicationTableCacheEntity?

    @Query("SELECT * FROM multiplication_table WHERE difficulty = :difficulty")
    suspend fun getMultiplicationTablesByDifficulty(difficulty: Difficulty): List<MultiplicationTableCacheEntity>?


    @Query("SELECT * FROM multiplication_table")
    suspend fun getAllMultiplicationTables(): List<MultiplicationTableCacheEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiplicationTable(multiplicationTableCacheEntity: MultiplicationTableCacheEntity)

    @Query("DELETE FROM multiplication_table")
    suspend fun clearMultiplicationTableCache()
}
