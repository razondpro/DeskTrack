package com.wagit.desktrack.ui.view.activites

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wagit.desktrack.databinding.ActivityLoginBinding
import com.wagit.desktrack.utils.Validator

class LoginActivity: BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val cifLD = MutableLiveData<String>()
    private val pwLD = MutableLiveData<String>()
    private val isValidLD = MediatorLiveData<Boolean>().apply {
        this.value = false

        addSource(cifLD) { cif ->
            val pw = pwLD.value
            this.value = validateForm(cif, pw)
        }
        addSource(pwLD){ pw ->
            val cif = cifLD.value
            this.value = validateForm(cif, pw)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etInputCif.doOnTextChanged { text, _, _, _ ->
            cifLD.value = text?.toString()
        }

        binding.etInputPW.doOnTextChanged { text, _, _, _ ->
            pwLD.value = text?.toString()
        }

        isValidLD.observe(this){ isValid ->
            //TODO Implement behaviors for form validation
        }
    }

    private fun validateForm(cif: String?, pw: String?): Boolean{
        return Validator.isValidCIF(cif) && !pw.isNullOrBlank()
    }
}