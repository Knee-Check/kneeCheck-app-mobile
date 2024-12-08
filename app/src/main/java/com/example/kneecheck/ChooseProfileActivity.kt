package com.example.kneecheck

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kneecheck.databinding.ActivityChooseProfileBinding
import com.example.kneecheck.databinding.ActivityLoginBinding

class ChooseProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChooseProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChooseProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.opsidokter.setOnClickListener {
            //move to register dokter
            val intent = Intent(baseContext, RegisterDokterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.opsipasien.setOnClickListener {
            //move to register pasien
            val intent = Intent(baseContext, RegisterPasienActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}