package com.jdlstudios.multiplicationmasterapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.repositories.interfacesimpl.SessionRepositoryImpl
import kotlinx.coroutines.launch

class SessionHistoryViewModel(
    private val sessionRepositoryImpl: SessionRepositoryImpl
): ViewModel() {

    private val _listSession = MutableLiveData<List<SessionEntity>>()
    val listSession: LiveData<List<SessionEntity>>
        get() = _listSession

    init {
        getListAllSessions()
    }

    private fun getListAllSessions() {
        viewModelScope.launch {
            sessionRepositoryImpl.allSessions.let { listFlow ->
                listFlow.collect {
                    _listSession.value = it
                }
            }
        }
    }

    fun deleteAllSessions(){
        viewModelScope.launch {
            sessionRepositoryImpl.deleteAllSessions()
        }
    }


}