package com.wagit.desktrack.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wagit.desktrack.data.entities.Employee
import com.wagit.desktrack.databinding.ActivityLoginBinding
import com.wagit.desktrack.ui.BaseActivity
import com.wagit.desktrack.ui.user.view.HomeActivity
import com.wagit.desktrack.ui.login.viewmodel.LoginViewModel
import com.wagit.desktrack.utils.Validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginVM: LoginViewModel by viewModels()

    private val emailLD = MutableLiveData<String>()
    private val pwLD = MutableLiveData<String>()
    private val isValidLD = MediatorLiveData<Boolean>().apply {
        this.value = false

        addSource(emailLD) { email ->
            val pw = pwLD.value
            this.value = validate(email, pw)
        }
        addSource(pwLD){ pw ->
            val email = emailLD.value
            this.value = validate(email, pw)
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
        this.attemptLogin()
    }

    /**
     * Handles login button click
     */
    private fun handleClick(){
        binding.btnLogin.setOnClickListener {
            if(isValidLD.value as Boolean){
                loginVM.logUser(emailLD.value.toString(), pwLD.value.toString())
            }else {
                Toast.makeText(applicationContext, "Please, insert valid data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Observes user livedata
     */
    private fun attemptLogin() {
        loginVM.user.observe(this, Observer {
            if(it.isEmpty()){
                Toast.makeText(applicationContext, "Wrong credentials", Toast.LENGTH_LONG).show()
            }else {
                val user: Employee = loginVM.user.value!!.first()
                goHomePage(user)
            }
        })
    }

    private fun goHomePage(user: Employee) {
        Intent(this, HomeActivity::class.java).also {
            it.putExtra("EXTRA_USER", user)
            startActivity(it)
            finish()
        }
        if(user.isAdmin!!){
            //go admin home activiy
        }else {
            //go user activity
        }
    }

    /**
     * form text change listeners
     */
    private fun validateLoginForm(){
        binding.etInputEmail.doOnTextChanged { text, _, _, _ ->
            emailLD.value = text?.toString()
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
     * Validate email and password
     */
    private fun validate(email: String?, pw: String?): Boolean{
        return Validator.isValidEmail(email) && !pw.isNullOrBlank() && pw.length >=6
    }
}