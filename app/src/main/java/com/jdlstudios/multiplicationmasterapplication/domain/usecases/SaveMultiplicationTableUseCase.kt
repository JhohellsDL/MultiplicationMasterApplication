package com.jdlstudios.multiplicationmasterapplication.domain.usecases

import com.jdlstudios.multiplicationmasterapplication.data.models.MultiplicationTable
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfaces.MultiplicationTableRepository
import com.jdlstudios.multiplicationmasterapplication.domain.models.MultiplicationTableItem
import com.jdlstudios.multiplicationmasterapplication.domain.models.toRepository

class SaveMultiplicationTableUseCase(
    private val repository: MultiplicationTableRepository
) {
    suspend fun saveMultiplicationTable(multiplicationTableItem: MultiplicationTableItem): MultiplicationTable {
        return repository.saveMultiplicationTable(multiplicationTableItem.toRepository())
    }
}