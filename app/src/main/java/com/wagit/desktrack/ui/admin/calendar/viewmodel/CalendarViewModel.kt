package com.wagit.desktrack.ui.admin.calendar.viewmodel

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
class CalendarViewModel  @Inject constructor(
    private val registryRepository: RegistryRepository,
) : ViewModel(){

    private val _allMonthRegistry: MutableLiveData<List<Registry>> = MutableLiveData()
    val allMonthRegistry: LiveData<List<Registry>> get() = _allMonthRegistry

    fun getAllRegistriesByMonthAndYear(month: String, year: String): LiveData<List<Registry>> {
        println("PARAMETROS En El getAllRegistriesByMonthAndYear  ******************** ${month} y " +
                "${year} ")
        viewModelScope.launch(Dispatchers.Main) {
            _allMonthRegistry.postValue(
                registryRepository.getAllRegistriesByMonthAndYear(month,year))
            //Log.d("HomeViewModel","Esto es el employer ID, startedAt y endedAt despues del postValue ${_tRegistry.value?.first()?.employeeId} ${_tRegistry.value?.first()?.startedAt} ${_tRegistry.value?.first()?.endedAt}")
        }
        println("RESULTADO En El getAllRegistriesByMonthAndYear " +
                "-------------------- ${allMonthRegistry.value}")
        return allMonthRegistry
    }


}