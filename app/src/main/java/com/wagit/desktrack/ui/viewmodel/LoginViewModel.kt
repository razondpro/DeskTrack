package com.wagit.desktrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wagit.desktrack.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun logUser(cif: String, pw: String){

        viewModelScope.launch {
            //testing
            println("viewmodel")
            println(userRepository.getUserByCifAndPsw(cif,pw).collect { print(it) })
        }
    }

}