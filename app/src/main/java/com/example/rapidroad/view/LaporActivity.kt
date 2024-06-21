package com.example.rapidroad.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.rapidroad.databinding.ActivityLaporBinding
import com.example.rapidroad.utils.getImageUri
import com.example.rapidroad.utils.getRealPathFromURI
import com.example.rapidroad.viewmodel.LaporViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Locale
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.asLiveData
import com.example.rapidroad.data.preference.UserPreference
import com.example.rapidroad.data.preference.dataStore

@Suppress("DEPRECATION")
class LaporActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaporBinding
    private var newImageUri: Uri? = null
    private val currentImageUri: MutableLiveData<Uri?> = MutableLiveData<Uri?>()
    private val viewModel: LaporViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userPreference: UserPreference
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        userPreference = UserPreference.getInstance(dataStore)
        userPreference.getSession().asLiveData().observe(this) { userModel ->
            userId = userModel.userId
            Log.d("LaporActivity", "User ID: $userId")

        }
        currentImageUri.observe(this) { uri ->
            uri?.let {
                binding.ivPreview.setImageURI(it)
            }
        }
        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.buttonAdd.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                getMyLocation()
            }
        }
        viewModel.addStorySuccess.observe(this, Observer { success ->
            if (success) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to add story", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    getAddressFromLocation(it)
                    println("Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                }
            }
            .addOnFailureListener {
                println("Failed to get location")
            }
    }


    private fun getAddressFromLocation(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val kecamatan = address.locality ?: "Unknown"
            val addressLine = address.getAddressLine(0)
            val specificPart = addressLine?.split(",")?.getOrNull(1)?.trim() ?: "Unknown"
            val street = address.featureName ?: "Unknown"
            val city = address.subAdminArea ?: "Unknown"
            addStory(location, city, kecamatan, specificPart, street)
            println("ID USER :${userId}")

        } else {
            Toast.makeText(this, "Failed to get address", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addStory(
        location: Location,
        city: String,
        kecamatan: String,
        village: String,
        street: String
    ) {
        val description = binding.edAddDescription.text.toString()
        val filePath = currentImageUri.value?.let { getRealPathFromURI(this, it) }
        if (filePath != null) {
            val file = File(filePath)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val id =
               userId.toRequestBody("text/plain".toMediaTypeOrNull())
            val kota = city.toRequestBody("text/plain".toMediaTypeOrNull())
            val desa = village.toRequestBody("text/plain".toMediaTypeOrNull())
            val kecamatan = kecamatan.toRequestBody("text/plain".toMediaTypeOrNull())
            val nama_jalan = street.toRequestBody("text/plain".toMediaTypeOrNull())
            val keterangan = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val longitude =
                location.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val latitude =
                location.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            viewModel.addStory(
                body,
                id,
                kota,
                desa,
                kecamatan,
                nama_jalan,
                keterangan,
                longitude,
                latitude
            )
        } else {

            Toast.makeText(this, "Failed to get file path", Toast.LENGTH_SHORT).show()

        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri.value = newImageUri
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri.value?.let {
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun startCamera() {
        newImageUri = getImageUri(this)
        launcherIntentCamera.launch(newImageUri!!)
    }


}
