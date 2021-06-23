package com.wagit.desktrack.ui.user.viewmodel

import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.Registry
import com.wagit.desktrack.data.entities.Account
import com.wagit.desktrack.data.repositories.RegistryRepository
import com.wagit.desktrack.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedHomeViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val registry: RegistryRepository
    ) : ViewModel() {

    private val _account: MutableLiveData<Account> = MutableLiveData()
    val account: MutableLiveData<Account> get() = _account

    private val _registries: MutableLiveData<List<Registry>> = MutableLiveData()
    val registries: LiveData<List<Registry>> get() = _registries

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    val text: LiveData<String> = _text

}