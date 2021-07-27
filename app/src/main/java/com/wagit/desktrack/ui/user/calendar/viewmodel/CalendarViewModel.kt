package com.wagit.desktrack.ui.user.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.repositories.RegistryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val registryRepository: RegistryRepository,
) : ViewModel() {
    private val _tRegistry: MutableLiveData<List<Registry>> = MutableLiveData()
    val tRegistry: LiveData<List<Registry>> get() = _tRegistry

    /**
     * Get today's registry from database.
     * //TODO _tRegistry should be update in init{} passing empId to VM constructor
     */
    fun getTodaysRegistry(empId: Long): LiveData<List<Registry>> {
        Log.d("HomeViewModel","Esto es el employer ID ${empId}")
        viewModelScope.launch(Dispatchers.IO) {
            _tRegistry.postValue(registryRepository.getTodaysRegByEmployee(empId))
            //Log.d("HomeViewModel","Esto es el employer ID, startedAt y endedAt despues del postValue ${_tRegistry.value?.first()?.employeeId} ${_tRegistry.value?.first()?.startedAt} ${_tRegistry.value?.first()?.endedAt}")
        }
        return tRegistry
    }
}