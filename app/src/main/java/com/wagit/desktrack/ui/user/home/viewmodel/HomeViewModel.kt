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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val registryRepository: RegistryRepository,
) : ViewModel() {

    private val _tRegistry: MutableLiveData<List<Registry>> = MutableLiveData()
    val tRegistry: LiveData<List<Registry>> get() = _tRegistry

    fun getTodaysRegistry(empId: Long): LiveData<List<Registry>> {
        viewModelScope.launch(Dispatchers.IO) {
            _tRegistry.postValue(registryRepository.getTodaysRegByEmployee(empId))
        }
        return tRegistry
    }

    fun checkIn() {
        viewModelScope.launch(Dispatchers.IO) {
            //  registryRepository.insert(Registry())
        }
    }
}