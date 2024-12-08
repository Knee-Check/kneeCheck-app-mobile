package com.example.kneecheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.ActivityRegisterPasienBinding
import com.example.kneecheck.entity.registerPasienDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterPasienActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterPasienBinding
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterPasienBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnDaftarPasien.setOnClickListener {
            val nama = binding.etNamaPasien.text.toString()
            val email = binding.etEmailPasien.text.toString()
            val password = binding.etPasswordPasien.text.toString()
            val gender = binding.etGenderPasien.text.toString()
            val domisili = binding.etDomisiliPasien.text.toString()
            val tanggalLahir = binding.etTanggalLahirPasien.text.toString()

            if (nama.isEmpty()){
                binding.etNamaPasien.error = "Nama tidak boleh kosong"
                binding.etNamaPasien.requestFocus()
                return@setOnClickListener
            }


            if (gender.isEmpty()) {
                binding.etGenderPasien.error = "Jenis Kelamin tidak boleh kosong"
                binding.etGenderPasien.requestFocus()
                return@setOnClickListener
            }

            if (gender != "Laki-laki" && gender != "Perempuan") {
                binding.etGenderPasien.error = "Jenis Kelamin harus Laki-laki atau Perempuan"
                binding.etGenderPasien.requestFocus()
                return@setOnClickListener
            }

            if (tanggalLahir.isEmpty()){
                binding.etTanggalLahirPasien.error = "Tanggal Lahir tidak boleh kosong"
                binding.etTanggalLahirPasien.requestFocus()
                return@setOnClickListener
            }

            //format tgl lahir yyyy-mm-dd
            if (!tanggalLahir.matches(Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}$"))) {
                binding.etTanggalLahirPasien.error = "Format Tanggal Lahir salah (yyyy-mm-dd)"
                binding.etTanggalLahirPasien.requestFocus()
                return@setOnClickListener
            }

            if (domisili.isEmpty()) {
                binding.etDomisiliPasien.error = "Kota Domisili tidak boleh kosong"
                binding.etDomisiliPasien.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()){
                binding.etEmailPasien.error = "Email tidak boleh kosong"
                binding.etEmailPasien.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                binding.etPasswordPasien.error = "Password tidak boleh kosong"
                binding.etPasswordPasien.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 8){
                binding.etPasswordPasien.error = "Password minimal 8 karakter"
                binding.etPasswordPasien.requestFocus()
                return@setOnClickListener
            }

            ioScope.launch {
                val pasien = registerPasienDTO(
                    name = nama,
                    email = email,
                    password = password,
                    gender = gender,
                    address = domisili,
                    birth = tanggalLahir
                )

                try {
                    val response = repo.registerPasien(pasien)
                    mainScope.launch {
                        Toast.makeText(this@RegisterPasienActivity, "Berhasil daftar!", Toast.LENGTH_SHORT).show()

                        onBackPressed()
                    }
                } catch (e: Exception) {
                    Log.e("Error Register", e.message.toString())
                    mainScope.launch {
                        Toast.makeText(this@RegisterPasienActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}