package com.wagit.desktrack.ui.user.profile.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wagit.desktrack.data.entities.Company
import com.wagit.desktrack.data.repositories.CompanyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val companyRepository: CompanyRepository,
) : ViewModel() {

    private val _company: MutableLiveData<List<Company>> = MutableLiveData()
    val company: LiveData<List<Company>> get() = _company

    fun getCompany(companyId: Long): LiveData<List<Company>>{
        Log.d("ProfileViewModel","Esto es el company ID ${companyId}")
        viewModelScope.launch(Dispatchers.IO) {
            _company.postValue(companyRepository.getCompany(companyId))
        }
        Log.d("ProfileViewModel","Esto es el company ${company.value?.first()?.name}")
        return company
    }

}