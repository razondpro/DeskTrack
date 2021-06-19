package com.wagit.desktrack.ui.viewmodel

import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _user: MutableLiveData<List<User>> = MutableLiveData()
    val user: LiveData<List<User>> get() = _user

    fun logUser(cif: String, pw: String){
        viewModelScope.launch {
            _user.value = userRepository.getUserByCifAndPsw(cif,pw)
        }
    }
}