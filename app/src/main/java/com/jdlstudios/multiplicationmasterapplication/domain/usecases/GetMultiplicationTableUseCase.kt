package com.jdlstudios.multiplicationmasterapplication.domain.usecases

import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.data.repositories.MultiplicationTableRepository

class GetMultiplicationTableUseCase(
    private val repository: MultiplicationTableRepository
) {

    suspend fun getMultiplicationTables(): List<MultiplicationTable> {
        return repository.getMultiplicationTables()
    }
}