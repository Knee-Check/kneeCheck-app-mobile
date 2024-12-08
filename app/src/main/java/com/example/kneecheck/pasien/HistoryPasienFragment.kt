package com.example.kneecheck.pasien

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
import com.example.kneecheck.databinding.FragmentHistoryPasienBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryPasienFragment : Fragment() {

    private var _binding: FragmentHistoryPasienBinding? = null

    private val binding get() = _binding!!

    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var histPasienAdapter: HistoryPasienAdapter

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryPasienBinding.inflate(inflater, container, false)
        val root: View = binding.root

        id = requireActivity().intent.getStringExtra("id").toString()
        name = requireActivity().intent.getStringExtra("name").toString()
        token = requireActivity().intent.getStringExtra("token").toString()

        ioScope.launch {
            try{
                val data = repo.getHistoryPasien(token)
                mainScope.launch {
                    binding.rvHistoryPasien.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false)
                    histPasienAdapter = HistoryPasienAdapter(data.data){ item ->
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
                        bundle.putString("asal_activity", "pasien")

                        findNavController().navigate(R.id.action_navigation_history_pasien_to_detailHistoryFragment2, bundle)
                    }
                    binding.rvHistoryPasien.adapter = histPasienAdapter
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