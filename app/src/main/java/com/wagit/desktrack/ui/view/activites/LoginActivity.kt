package com.wagit.desktrack.ui.view.activites

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wagit.desktrack.databinding.ActivityLoginBinding
import com.wagit.desktrack.ui.viewmodel.LoginViewModel
import com.wagit.desktrack.utils.Validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

   private val loginVM: LoginViewModel by viewModels()

    private val cifLD = MutableLiveData<String>()
    private val pwLD = MutableLiveData<String>()
    private val isValidLD = MediatorLiveData<Boolean>().apply {
        this.value = false

        addSource(cifLD) { cif ->
            val pw = pwLD.value
            this.value = validate(cif, pw)
        }
        addSource(pwLD){ pw ->
            val cif = cifLD.value
            this.value = validate(cif, pw)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView(){
        this.validateLoginForm()
        this.handleClick()
    }

    private fun handleClick(){
        binding.btnLogin.setOnClickListener {
            if(isValidLD.value as Boolean){
                Log.v("isvalid", isValidLD.value.toString())
                loginVM.logUser(cifLD.value.toString(), pwLD.value.toString())
            }else {
                Toast.makeText(applicationContext, "Please, insert valid data", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * form text change listeners
     */
    private fun validateLoginForm(){
        binding.etInputCif.doOnTextChanged { text, _, _, _ ->
            cifLD.value = text?.toString()
        }

        binding.etInputPW.doOnTextChanged { text, _, _, _ ->
            pwLD.value = text?.toString()
        }

        isValidLD.observe(this){ isValid ->
            //TODO Implement behaviors after form validation
                Log.v("isvalid", isValid.toString())
        }
    }

    /**
     * Validate cif and password
     */
    private fun validate(cif: String?, pw: String?): Boolean{
        return Validator.isValidCIF(cif) && !pw.isNullOrBlank() && pw.length >=6
    }
}