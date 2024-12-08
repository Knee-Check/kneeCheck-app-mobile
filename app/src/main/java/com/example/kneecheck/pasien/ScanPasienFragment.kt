package com.example.kneecheck.pasien

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kneecheck.R
import com.example.kneecheck.config.ApiConfiguration
import com.example.kneecheck.config.DefaultRepo
import com.example.kneecheck.databinding.FragmentScanPasienBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ScanPasienFragment : Fragment() {
    private var _binding: FragmentScanPasienBinding? = null
    private val binding get() = _binding!!
    private lateinit var getContent: ActivityResultLauncher<String>
    private var repo: DefaultRepo = ApiConfiguration.defaultRepo
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var id :String
    private lateinit var name :String
    private lateinit var token :String
    private lateinit var uriImage : Uri
    private var hasUpload: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScanPasienBinding.inflate(inflater, container, false)
        val root: View = binding.root

        id = requireActivity().intent.getStringExtra("id").toString()
        name = requireActivity().intent.getStringExtra("name").toString()
        token = requireActivity().intent.getStringExtra("token").toString()

        binding.tvNamaPasien.text = name

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val resizedBitmap = resizeImage(uri, 500, 500)
                binding.ivHasilUploadScanPasien.setImageBitmap(resizedBitmap)
                hasUpload = true

                uriImage = uri
                getImageSize()
            }
        }

        binding.ibGaleryScanPasien.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.ibPeriksaScanPasien.setOnClickListener {
            if (hasUpload)
                uploadImage()
            else
                Toast.makeText(requireContext(), "Upload X-Ray terlebih dahulu", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    private fun resizeImage(uri: Uri, width: Int, height: Int): Bitmap? {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun uploadImage() {
        val file = uriToFile(uriImage)
        val mimeType = getMimeType(file)
        if (mimeType !in listOf("image/png", "image/jpeg", "image/jpg")) {
            Toast.makeText(requireContext(), "Unsupported file format!", Toast.LENGTH_SHORT).show()
            Log.e("Upload", "Unsupported file format!")
            return
        }
        val requestFile: RequestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("img", file.name, requestFile)

        ioScope.launch {
            try {
                val response = repo.predict(token, body)
                mainScope.launch {
                    Log.d("Upload", "Image uploaded: $response")

                    val bundle = Bundle()
                    bundle.putInt("confidenceScore", response.data.confidenceScore)
                    bundle.putString("label", response.data.label)
                    bundle.putString("id_xray", response.data.id_xray)
                    bundle.putString("img_path", response.data.img_path)
                    bundle.putString("pengobatan", response.data.pengobatan)

                    findNavController().navigate(R.id.action_navigation_scan_pasien_to_hasilPredictPasienFragment, bundle)
                }
            } catch (e: Exception) {
                Log.e("Upload", "Error uploading image: ${e.message}")
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "img.png")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    fun getImageSize(): Long {
        val file = uriToFile(uriImage)
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        val fileSizeInMB = fileSizeInKB / 1024
        Log.d("Image Size", "Size: $fileSizeInBytes bytes ($fileSizeInKB KB, $fileSizeInMB MB)")
        return fileSizeInBytes
    }

    private fun getMimeType(file: File): String {
        val extension = file.extension
        return when (extension.lowercase()) {
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            else -> "unknown"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}