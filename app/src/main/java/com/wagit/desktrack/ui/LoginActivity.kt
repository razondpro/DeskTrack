package com.wagit.desktrack.ui
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wagit.desktrack.R
import com.wagit.desktrack.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}