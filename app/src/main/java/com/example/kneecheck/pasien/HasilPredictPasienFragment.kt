package com.example.kneecheck.pasien

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kneecheck.R
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.FragmentHasilPredictBinding
import com.example.kneecheck.databinding.FragmentHasilPredictPasienBinding
import com.example.kneecheck.entity.saveHistoryPasienDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HasilPredictPasienFragment : Fragment() {
    private lateinit var binding : FragmentHasilPredictPasienBinding
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHasilPredictPasienBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val confidenceScore = arguments?.getInt("confidenceScore")
        val label = arguments?.getString("label")
        val id_xray = arguments?.getString("id_xray")
        val img_path = arguments?.getString("img_path")
        val pengobatan = arguments?.getString("pengobatan")

        id = requireActivity().intent.getStringExtra("id").toString()
        name = requireActivity().intent.getStringExtra("name").toString()
        token = requireActivity().intent.getStringExtra("token").toString()

        binding.tvLabelKeparahanScanPasien.text = label
        binding.tvTipsPengobatanScanPasien.text = pengobatan

        binding.btnBackToScanPasien.setOnClickListener {
            findNavController().navigate(R.id.action_hasilPredictPasienFragment_to_navigation_scan_pasien)
        }

        binding.btnSimpanKeHistoryPasien.setOnClickListener {
            ioScope.launch {
                try{
                    val historyPasien = saveHistoryPasienDTO(
                        id_xray = id_xray!!,
                        img = img_path!!,
                        confidence_score = confidenceScore!!,
                        label = label!!
                    )
                    val data = repo.saveHistoryPasien(token, historyPasien)
                    mainScope.launch {
                        Toast.makeText(requireContext(), "Berhasil Simpan ke Histori", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_hasilPredictPasienFragment_to_navigation_scan_pasien)
                    }
                } catch (e:Exception){
                    Log.e("Error API Save History Pasien", e.message.toString())
                    Log.e("Error API Save History Pasien", e.toString())
                }
            }
        }

        return root
    }
}