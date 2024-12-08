package com.example.kneecheck.dokter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kneecheck.R
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.FragmentPasienSudahPunyaAkunBinding
import com.example.kneecheck.entity.pasienData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasienSudahPunyaAkunFragment : Fragment() {
    private var _binding: FragmentPasienSudahPunyaAkunBinding? = null
    private val binding get() = _binding!!
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var pasienAdapter : HasilPencarianAdapter

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasienSudahPunyaAkunBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val confidenceScore = arguments?.getInt("confidenceScore")!!
        val label = arguments?.getString("label")!!
        val id_xray = arguments?.getString("id_xray")!!
        val img_path = arguments?.getString("img_path")!!
        val pengobatan = arguments?.getString("pengobatan")!!

        id = requireActivity().intent.getStringExtra("id").toString()
        name = requireActivity().intent.getStringExtra("name").toString()
        token = requireActivity().intent.getStringExtra("token").toString()

        binding.rvHasilPencarian.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        ioScope.launch {
            try {
                val data = repo.getAllPasien(token)
                mainScope.launch {
                    val dataPasien = data.data
                    var filteredData = dataPasien

                    pasienAdapter = HasilPencarianAdapter(filteredData) { item ->
                        val bundle = Bundle()
                        bundle.putInt("confidenceScore", confidenceScore)
                        bundle.putString("label", label)
                        bundle.putString("id_xray", id_xray)
                        bundle.putString("img_path", img_path)
                        bundle.putString("pengobatan", pengobatan)
                        bundle.putString("id_pasien", item.id_pasien)
                        bundle.putString("name_pasien", item.name)
                        bundle.putString("gender", item.gender)
                        bundle.putString("birth", item.birth.substring(0, 10))
                        bundle.putString("address", item.address)
                        bundle.putString("akun", "sudah")
                        findNavController().navigate(R.id.action_pasienSudahPunyaAkunFragment_to_dataPasienDanHasilPredictFragment, bundle)
                    }

                    binding.rvHasilPencarian.adapter = pasienAdapter
                    pasienAdapter.notifyDataSetChanged()

                    binding.ibCariPasien.setOnClickListener {
                        Log.d("DATAAA PASIEN", filteredData.toString())
                        Log.d("DATAAA PASIEN1"," MASOK COK")
                        val searchText = binding.etNamaPasienCariPasien.text
                        if ( searchText != null && searchText.toString().isNotEmpty() ) {
                            filteredData = dataPasien.filter {
                                it.name.contains(searchText.toString(), ignoreCase = true) ||
                                it.email.contains(searchText.toString(), ignoreCase = true)
                            }
                        } else {
                            filteredData = dataPasien
                        }

                        Log.d("DATAAA PASIEN2", filteredData.toString())

                        pasienAdapter = HasilPencarianAdapter(filteredData) { item ->
                            val bundle = Bundle()
                            bundle.putInt("confidenceScore", confidenceScore)
                            bundle.putString("label", label)
                            bundle.putString("id_xray", id_xray)
                            bundle.putString("img_path", img_path)
                            bundle.putString("pengobatan", pengobatan)
                            bundle.putString("id_pasien", item.id_pasien)
                            bundle.putString("name_pasien", item.name)
                            bundle.putString("gender", item.gender)
                            bundle.putString("birth", item.birth.substring(0, 10))
                            bundle.putString("address", item.address)
                            bundle.putString("akun", "sudah")
                            findNavController().navigate(R.id.action_pasienSudahPunyaAkunFragment_to_dataPasienDanHasilPredictFragment, bundle)
                        }

                        binding.rvHasilPencarian.adapter = pasienAdapter
                        pasienAdapter.notifyDataSetChanged()
                    }
                }

            } catch (e: Exception) {
                Log.e("ERROR", e.toString())
            }
        }

        binding.ibTambahPasienCariPasien.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("confidenceScore", confidenceScore)
            bundle.putString("label", label)
            bundle.putString("id_xray", id_xray)
            bundle.putString("img_path", img_path)
            bundle.putString("pengobatan", pengobatan)

            findNavController().navigate(R.id.action_pasienSudahPunyaAkunFragment_to_pasienBelumPunyaAkunFragment, bundle)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}