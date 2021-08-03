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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val registryRepository: RegistryRepository,
) : ViewModel() {

    private val _monthRegistry: MutableLiveData<List<Registry>> = MutableLiveData()
    val monthRegistry: LiveData<List<Registry>> get() = _monthRegistry


    //Esta función obtiene todos los registros del mes actual dado el usuario

    fun getAllRegistriesByEmployeeAndMonthAndYear(empId: Long, month: String, year: String): LiveData<List<Registry>> {
        println("PARAMETROS ******************** ${empId} y ${month} y ${year} ")
        viewModelScope.launch(Dispatchers.Main) {
            _monthRegistry.postValue(registryRepository.getAllRegistriesByEmployeeAndMonthAndYear(empId,month,year))
            //Log.d("HomeViewModel","Esto es el employer ID, startedAt y endedAt despues del postValue ${_tRegistry.value?.first()?.employeeId} ${_tRegistry.value?.first()?.startedAt} ${_tRegistry.value?.first()?.endedAt}")
        }
        println("RESULTADO -------------------- ${monthRegistry.value}")
        return monthRegistry
    }


}