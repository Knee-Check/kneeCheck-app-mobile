package com.example.kneecheck.dokter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kneecheck.R
import com.example.kneecheck.databinding.FragmentPasienBelumPunyaAkunBinding

class PasienBelumPunyaAkunFragment : Fragment() {
    private lateinit var binding : FragmentPasienBelumPunyaAkunBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasienBelumPunyaAkunBinding.inflate(inflater, container, false)
        val root = binding.root

        val confidenceScore = arguments?.getInt("confidenceScore")!!
        val label = arguments?.getString("label")!!
        val id_xray = arguments?.getString("id_xray")!!
        val img_path = arguments?.getString("img_path")!!
        val pengobatan = arguments?.getString("pengobatan")!!

        binding.imageButton.setOnClickListener {
            val name_pasien = binding.etNamaPasienBelumPunyaAkun.text.toString()
            val gender = binding.etGenderPasienBelumPunyaAkun.text.toString()
            val birth = binding.etBirthPasienBelumPunyaAkun.text.toString()
            val domisili = binding.etKotaPasienBelumPunyaAkun.text.toString()

            // Arahkan ke halaman pendaftaran
            val bundle = Bundle()
            bundle.putInt("confidenceScore", confidenceScore)
            bundle.putString("label", label)
            bundle.putString("id_xray", id_xray)
            bundle.putString("img_path", img_path)
            bundle.putString("pengobatan", pengobatan)
            bundle.putString("name_pasien", name_pasien)
            bundle.putString("gender", gender)
            bundle.putString("birth", birth)
            bundle.putString("address", domisili)
            bundle.putString("akun", "belum")

            findNavController().navigate(R.id.action_pasienBelumPunyaAkunFragment_to_dataPasienDanHasilPredictFragment, bundle)

        }

        return root
    }

}