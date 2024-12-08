package com.example.kneecheck.dokter

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.example.kneecheck.R
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.FragmentHasilPredictBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HasilPredictFragment : Fragment() {
    private lateinit var binding : FragmentHasilPredictBinding

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHasilPredictBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val confidenceScore = arguments?.getInt("confidenceScore")
        val label = arguments?.getString("label")
        val id_xray = arguments?.getString("id_xray")
        val img_path = arguments?.getString("img_path")
        val pengobatan = arguments?.getString("pengobatan")

        id = requireActivity().intent.getStringExtra("id").toString()
        name = requireActivity().intent.getStringExtra("name").toString()
        token = requireActivity().intent.getStringExtra("token").toString()

        binding.tvLabelKeparahan.text = label
        binding.tvTipsPengobatan.text = pengobatan

        binding.btnBackToScan.setOnClickListener {
            findNavController().navigate(R.id.action_hasilPredictFragment_to_navigation_scan)
        }

        binding.btnSimpanKeHistory.setOnClickListener {
            dialog(root, confidenceScore!!, label!!, id_xray!!, img_path!!, pengobatan!!)
        }

        return root
    }

    private fun dialog(
        view: View,
        confidenceScore: Int,
        label : String,
        id_xray : String,
        img_path : String,
        pengobatan : String
    ) {
        val bundle = Bundle()
        bundle.putInt("confidenceScore", confidenceScore)
        bundle.putString("label", label)
        bundle.putString("id_xray", id_xray)
        bundle.putString("img_path", img_path)
        bundle.putString("pengobatan", pengobatan)

        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setMessage("Apakah pasien sudah punya akun?")

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sudah"
        ) { dialog, which ->
            dialog.dismiss()
            //pindah ke halaman sudah
            findNavController().navigate(R.id.action_hasilPredictFragment_to_pasienSudahPunyaAkunFragment, bundle)
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Belum"
        ) { dialog, which ->
            dialog.dismiss()
            //pindah ke halaman belum
            findNavController().navigate(R.id.action_hasilPredictFragment_to_pasienBelumPunyaAkunFragment, bundle)
        }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnPositive.backgroundTintList = resources.getColorStateList(R.color.btnNo)
        btnNegative.layoutParams = layoutParams
        btnNegative.backgroundTintList = resources.getColorStateList(R.color.btnYes)
    }
}