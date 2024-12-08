package com.example.kneecheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.ActivityLoginBinding
import com.example.kneecheck.entity.loginDTO
import com.example.kneecheck.pasien.LandingPageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private var repo: DefaultRepo? = null
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            ApiConfiguration.getApiService()
            repo = ApiConfiguration.defaultRepo
            Log.d("API", "API Connected")
        } catch (e : Error){
            Log.e("Error API", e.message.toString())
        }
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty()) {
                binding.emailEditText.error = "Email tidak boleh kosong"
                binding.emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordEditText.error = "Password tidak boleh kosong"
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }

            ioScope.launch {
                val login = loginDTO(
                    email = email,
                    password = password
                )
                try {
                    val res = repo?.login(login)
                    mainScope.launch {
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                        val intent: Intent
                        if (res?.userType == "Pasien") {
                            intent = Intent(this@LoginActivity, LandingPageActivity::class.java)
                        } else {
                            intent = Intent(this@LoginActivity, DokterActivity::class.java)
                        }
                        intent.putExtra("id", res?.id)
                        intent.putExtra("name", res?.name)
                        intent.putExtra("token", "Bearer " + res?.token)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    Log.e("Error Login", e.message.toString())
                    mainScope.launch {
                        Toast.makeText(this@LoginActivity, "email atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.daftar.setOnClickListener {
            val intent = Intent(baseContext, ChooseProfileActivity::class.java)
            startActivity(intent)
        }

    }
}