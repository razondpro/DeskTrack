package com.wagit.desktrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.wagit.desktrack.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


}