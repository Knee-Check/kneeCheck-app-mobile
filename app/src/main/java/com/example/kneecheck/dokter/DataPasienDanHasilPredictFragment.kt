package com.example.kneecheck.dokter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.kneecheck.R
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.FragmentDataPasienDanHasilPredictBinding
import com.example.kneecheck.entity.saveHistoryPasienBaruDTO
import com.example.kneecheck.entity.saveHistoryPasienLamaDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataPasienDanHasilPredictFragment : Fragment() {
    private lateinit var binding : FragmentDataPasienDanHasilPredictBinding
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataPasienDanHasilPredictBinding.inflate(inflater, container, false)
        val root = binding.root

        id = requireActivity().intent.getStringExtra("id").toString()
        name = requireActivity().intent.getStringExtra("name").toString()
        token = requireActivity().intent.getStringExtra("token").toString()

        val confidenceScore = arguments?.getInt("confidenceScore")
        val label = arguments?.getString("label")
        val id_xray = arguments?.getString("id_xray")
        val img_path = arguments?.getString("img_path")
        val pengobatan = arguments?.getString("pengobatan")
        val namePasien = arguments?.getString("name_pasien")
        val gender = arguments?.getString("gender")
        val birth = arguments?.getString("birth")
        val address = arguments?.getString("address")
        val akun = arguments?.getString("akun")

        binding.tvTanggalScanData.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        binding.tvLabelKeparahanData.text = label
        binding.tvIdXrayData.text = id_xray

        if (akun == "belum"){
            binding.tvIdPasien.isVisible = false
            binding.tvIdPasienData.isVisible = false
        } else {
            binding.tvIdPasienData.text = arguments?.getString("id_pasien")
        }

        binding.tvNamaLengkapPasienData.text = namePasien
        binding.tvBirthPasienData.text = birth
        binding.tvGenderPasienData.text = gender
        binding.tvAddressPasienData.text = address

        binding.ibSimpanKeHistoryDataPasien.setOnClickListener {
            if(akun == "belum") {
                ioScope.launch {
                    try {
                        val data = saveHistoryPasienBaruDTO(
                            id_xray = id_xray!!,
                            img = img_path!!,
                            confidence_score = confidenceScore!!,
                            label = label!!,
                            name = namePasien!!,
                            gender = gender!!,
                            birth = birth!!,
                            address = address!!
                        )
                        val res = repo.saveHistoryPasienBaru(token, data)
                        mainScope.launch {
                            Log.d("Save History Pasien", res.toString())
                            Toast.makeText(
                                requireContext(),
                                "Data Pasien Berhasil Disimpan",
                                Toast.LENGTH_SHORT
                            ).show()

                            //kembali ke halaman scan awal
                            findNavController().navigate(R.id.action_dataPasienDanHasilPredictFragment_to_navigation_scan)
                        }
                    } catch (e: Exception) {
                        Log.e("Error Simpan Data Ke History", e.message.toString())
                        mainScope.launch {
                            Log.e("Error Simpan Data Ke History", e.message.toString())
                        }
                    }
                }
            } else {
                //save data pasien lama
                ioScope.launch {
                    try {
                        val data = saveHistoryPasienLamaDTO(
                            id_xray = id_xray!!,
                            img = img_path!!,
                            confidence_score = confidenceScore!!,
                            label = label!!,
                            id_pasien = arguments?.getString("id_pasien")!!,
                        )
                        val res = repo.saveHistoryPasienLama(token, data)
                        mainScope.launch {
                            Log.d("Save History Pasien", res.toString())
                            Toast.makeText(
                                requireContext(),
                                "Data Pasien Berhasil Disimpan",
                                Toast.LENGTH_SHORT
                            ).show()

                            //kembali ke halaman scan awal
                            findNavController().navigate(R.id.action_dataPasienDanHasilPredictFragment_to_navigation_scan)
                        }
                    } catch (e: Exception) {
                        Log.e("Error Simpan Data Ke History", e.message.toString())
                        mainScope.launch {
                            Log.e("Error Simpan Data Ke History", e.message.toString())
                        }
                    }
                }
            }
        }

        return root
    }
}