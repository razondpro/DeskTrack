package com.wagit.desktrack.ui.user.home.viewmodel

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

    fun getTRegistryUpdatedFromDB(empId: Long): LiveData<List<Registry>> {
        viewModelScope.launch(Dispatchers.IO) {
            _tRegistry.postValue(registryRepository.getTodaysRegByEmployee(empId))
        }
        return tRegistry
    }

    /**
     * Creates a registry in db
     */
    fun checkIn(registry: Registry) {
        viewModelScope.launch(Dispatchers.IO) {
            registryRepository.insert(registry)
            _tRegistry.postValue(listOf(registry))
        }
    }

    /**
     * Updates a registry in db
     */
    fun checkOut(registry: Registry) {
        viewModelScope.launch(Dispatchers.IO) {
            registryRepository.update(registry)
            _tRegistry.postValue(listOf(registry))
        }
    }
}