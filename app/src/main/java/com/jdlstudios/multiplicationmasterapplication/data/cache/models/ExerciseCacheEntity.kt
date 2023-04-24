package com.jdlstudios.multiplicationmasterapplication.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise

@Entity(tableName = "exercise")
data class ExerciseCacheEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "session_id") val sessionId: Long,
    val operand1: Int,
    val operand2: Int,
    val answer: Int,
    val correct: Boolean
){
    fun toExercise(): Exercise{
        return Exercise(
            id = id,
            sessionId = sessionId,
            operand1 = operand1,
            operand2 = operand2,
            answer = answer,
            correct = correct
        )
    }
}
