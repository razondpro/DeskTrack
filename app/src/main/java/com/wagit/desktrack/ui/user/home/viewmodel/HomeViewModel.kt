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
class HomeViewModel @Inject constructor(
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
            Log.d("HomeViewModel","Esto es el employer ID despues del postValue ${_tRegistry.value}")
        }
        return tRegistry
    }

    /**
     * Creates a registry in db,then set new value to today's registry
     */
    fun checkIn(registry: Registry) {
        viewModelScope.launch(Dispatchers.IO) {
            registryRepository.insert(registry)
            _tRegistry.postValue(listOf(registry))
        }
    }

    /**
     * Updates a registry in db, then set new value to today's registry
     */
    fun checkOut(registry: Registry) {
        viewModelScope.launch(Dispatchers.IO) {
            registryRepository.update(registry)
            _tRegistry.postValue(listOf(registry))
        }
    }

}