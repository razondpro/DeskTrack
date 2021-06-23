package com.wagit.desktrack.ui.login.viewmodel

import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.Account
import com.wagit.desktrack.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository) : ViewModel() {

    private val _account: MutableLiveData<List<Account>> = MutableLiveData()
    val account: LiveData<List<Account>> get() = _account

    fun logUser(email: String, pw: String){
        viewModelScope.launch {
            _account.value = accountRepository.getAccoundByEmailAndPw(email,pw)
        }
    }
}