package com.example.kneecheck

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kneecheck.databinding.FragmentDetailHistoryBinding

class DetailHistoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailHistoryBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val idPasien = arguments?.getString("idPasien")
        val name = arguments?.getString("name")
        val gender = arguments?.getString("gender")
        val birth = arguments?.getString("birth")
        val address = arguments?.getString("address")
        val id_xray = arguments?.getString("id_xray")
        val img = arguments?.getString("img")
        val confidence_score = arguments?.getString("confidence_score")
        val label = arguments?.getString("label")
        val tgl_scan = arguments?.getString("tgl_scan")
        val asal_activity = arguments?.getString("asal_activity")
        val pengobatan = arguments?.getString("pengobatan")

        val umur = 2024 - (birth?.substring(birth.length-4)?.toInt() ?: 0)

        binding.tvTanggalScanDetailHistory.text = tgl_scan
        binding.tvNamaPasienDetailHistory.text = name
        binding.tvJenisKelaminDetailHistory.text = gender
        binding.tvUmurPasienDetailHistory.setText("$umur Tahun")
        binding.tvAlamatPasienDetailHistory.text = address

        binding.tvLabelKeparahanDetailHistory.text = confidence_score
        if(confidence_score == "0"){
            binding.tvLabelKeparahanDetailHistory.setTextColor(Color.parseColor("#00FF00"))
            binding.tvInfoSeverity.text = "Normal"
        } else if(confidence_score == "2"){
            binding.tvLabelKeparahanDetailHistory.setTextColor(Color.parseColor("#FFA500"))
            binding.tvInfoSeverity.text = "Knee Pain"
        } else if(confidence_score == "4"){
            binding.tvLabelKeparahanDetailHistory.setTextColor(Color.parseColor("#FF0000"))
            binding.tvInfoSeverity.text = "Severe Knee"
        }

        binding.tvTipsPengobatanDetailHistory.text = pengobatan

        binding.btnBackToHistory.setOnClickListener {
            if(asal_activity == "dokter"){
                findNavController().navigate(R.id.action_detailHistoryFragment_to_navigation_history)
            } else {
                findNavController().navigate(R.id.action_detailHistoryFragment2_to_navigation_history_pasien)
            }
        }

        Glide.with(requireContext())
            .load(img)
            .apply(RequestOptions().placeholder(R.drawable.logo))
            .into(binding.ivGambarXray)

        return root
    }

}