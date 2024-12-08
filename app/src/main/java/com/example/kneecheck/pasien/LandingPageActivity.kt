package com.example.kneecheck.pasien

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kneecheck.PasienActivity
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.ActivityLandingPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LandingPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLandingPageBinding
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("name").toString()
        token = intent.getStringExtra("token").toString()

        ioScope.launch {
            try{
                val data = repo.getLandingPage(token)
                mainScope.launch {
                    binding.tvKeseluruhanLanding.text = data.data.totalScanned.toString()

                    binding.tvAvgAgeLanding.text = data.data.age.average
                    binding.tvTotalAgeLanding.text = data.data.age.total_kasus.toString()

                    binding.tvAvgGenderLanding.text = data.data.gender.average
                    binding.tvTotalGenderLanding.text = data.data.gender.total_kasus.toString()
                }
            } catch (e:Exception){
                Log.e("Error API DASHBOARD", e.message.toString())
                Log.e("Error API DASHBOARD2", e.toString())
            }
        }

        binding.btnMulaiLanding.setOnClickListener {
            val intent: Intent = Intent(this@LandingPageActivity, PasienActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }
}