package com.wagit.desktrack.ui.viewmodel

import androidx.lifecycle.*
import com.wagit.desktrack.data.entities.User
import com.wagit.desktrack.data.repositories.RegistryRepository
import com.wagit.desktrack.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedHomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val registry: RegistryRepository
    ) : ViewModel() {

    private val _user: MutableLiveData<List<User>> = MutableLiveData()
    val user: LiveData<List<User>> get() = _user

    private val _registries: MutableLiveData<List<User>> = MutableLiveData()
    val registries: LiveData<List<User>> get() = _registries

}