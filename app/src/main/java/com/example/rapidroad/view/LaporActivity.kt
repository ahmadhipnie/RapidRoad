package com.example.rapidroad.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.rapidroad.databinding.ActivityLaporBinding
import com.example.rapidroad.utils.getImageUri
import com.google.android.gms.location.LocationServices

class LaporActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaporBinding
    private var newImageUri: Uri? = null
    private val currentImageUri: MutableLiveData<Uri?> = MutableLiveData<Uri?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
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
            .addOnSuccessListener { location : Location? ->
                location?.let {
                    println("Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                }
            }
            .addOnFailureListener {
                println("Failed to get location")
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
