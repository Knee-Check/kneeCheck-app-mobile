package com.example.kneecheck.dokter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kneecheck.R
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.FragmentHistoryBinding
import com.example.kneecheck.pasien.HistoryPasienAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var histDokterAdapter: HistoryDokterAdapter

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        id = requireActivity().intent.getStringExtra("id").toString()
        name = requireActivity().intent.getStringExtra("name").toString()
        token = requireActivity().intent.getStringExtra("token").toString()

        ioScope.launch {
            try{
                val data = repo.getHistoryDokter(token)
                Log.d("DATAAA HISTORY DOKTER", data.toString())
                mainScope.launch {
                    binding.rvHistoryDokter.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    histDokterAdapter = HistoryDokterAdapter(data.data) { item ->
                        //pindah ke halaman history detail
                        val bundle = Bundle()
                        bundle.putString("idPasien", item.id_pasien)
                        bundle.putString("name", item.name)
                        bundle.putString("gender", item.gender)
                        bundle.putString("birth", item.birth)
                        bundle.putString("address", item.address)
                        bundle.putString("id_xray", item.id_xray)
                        bundle.putString("img", item.img)
                        bundle.putString("confidence_score", item.confidence_score.toString())
                        bundle.putString("label", item.label)
                        bundle.putString("tgl_scan", item.tgl_scan)
                        bundle.putString("pengobatan", item.pengobatan)
                        bundle.putString("asal_activity", "dokter")

                        findNavController().navigate(R.id.action_navigation_history_to_detailHistoryFragment, bundle)
                    }
                    binding.rvHistoryDokter.adapter = histDokterAdapter
                }
            } catch (e:Exception){
                Log.e("Error API DASHBOARD", e.message.toString())
                Log.e("Error API DASHBOARD2", e.toString())
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}