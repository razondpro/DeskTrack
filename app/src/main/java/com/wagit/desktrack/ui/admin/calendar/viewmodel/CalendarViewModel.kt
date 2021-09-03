package com.wagit.desktrack.ui.admin.calendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.repositories.RegistryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel  @Inject constructor(
    private val registryRepository: RegistryRepository,
) : ViewModel(){

    private val _allMonthRegistry: MutableLiveData<List<Registry>> = MutableLiveData()
    val allMonthRegistry: LiveData<List<Registry>> get() = _allMonthRegistry



}