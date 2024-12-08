package com.example.kneecheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.ActivityRegisterDokterBinding
import com.example.kneecheck.entity.loginDTO
import com.example.kneecheck.entity.registerDokterDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterDokterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterDokterBinding
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnDaftarDokter.setOnClickListener {
            val nama = binding.etNamaDokter.text.toString()
            val email = binding.etEmailDokter.text.toString()
            val password = binding.etPasswordDokter.text.toString()
            val gender = binding.etGenderDokter.text.toString()
            val domisili = binding.etDomisiliDokter.text.toString()
            val instansi = binding.etInstansiDokter.text.toString()

            if (nama.isEmpty()) {
                binding.etNamaDokter.error = "Nama tidak boleh kosong"
                binding.etNamaDokter.requestFocus()
                return@setOnClickListener
            }

            if (gender.isEmpty()) {
                binding.etGenderDokter.error = "Jenis Kelamin tidak boleh kosong"
                binding.etGenderDokter.requestFocus()
                return@setOnClickListener
            }

            if (gender != "Laki-laki" && gender != "Perempuan") {
                binding.etGenderDokter.error = "Jenis Kelamin harus Laki-laki atau Perempuan"
                binding.etGenderDokter.requestFocus()
                return@setOnClickListener
            }

            if (domisili.isEmpty()) {
                binding.etDomisiliDokter.error = "Kota Domisili tidak boleh kosong"
                binding.etDomisiliDokter.requestFocus()
                return@setOnClickListener
            }

            if (instansi.isEmpty()) {
                binding.etInstansiDokter.error = "Instansi tidak boleh kosong"
                binding.etInstansiDokter.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.etEmailDokter.error = "Alamat Email tidak boleh kosong"
                binding.etEmailDokter.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailDokter.error = "Alamat Email tidak valid"
                binding.etEmailDokter.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPasswordDokter.error = "Kata Sandi tidak boleh kosong"
                binding.etPasswordDokter.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.etPasswordDokter.error = "Kata Sandi minimal 8 karakter"
                binding.etPasswordDokter.requestFocus()
                return@setOnClickListener
            }

            ioScope.launch {
                val dokter = registerDokterDTO(
                    name = nama,
                    email = email,
                    password = password,
                    gender = gender,
                    address = domisili,
                    hospital = instansi
                )

                try {
                    val response = repo.registerDokter(dokter)
                    mainScope.launch {
                        Toast.makeText(this@RegisterDokterActivity, "Berhasil daftar!", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                } catch (e: Exception) {
                    Log.e("Error Register", e.message.toString())
                    mainScope.launch {
                        Toast.makeText(this@RegisterDokterActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }
}