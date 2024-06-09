package com.example.rapidroad.view.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.rapidroad.R
import com.example.rapidroad.components.CustomDialogLoading
import com.example.rapidroad.data.repository.ResultState
import com.example.rapidroad.data.retrofit.response.DataItem
import com.example.rapidroad.viewmodel.MapsViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), GoogleMap.OnInfoWindowClickListener {

    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var dialogLoading: CustomDialogLoading

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        mMap.setOnInfoWindowClickListener(this)

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true

        showMarkerStories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        dialogLoading = CustomDialogLoading(requireContext())
    }

    private fun showMarkerStories() {
        lifecycleScope.launch {
            mapsViewModel.getAllReportLocation().observe(requireActivity()) { userLocation ->
                when (userLocation) {
                    is ResultState.Error -> {
                        dialogLoading.setLoadingVisible(false)
                        val error = userLocation.error
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Loading -> {
                        dialogLoading.setLoadingVisible(true)
                    }
                    is ResultState.Success -> {
                        dialogLoading.setLoadingVisible(false)
                        userLocation.data.forEach { data ->
                            val latLng = LatLng(data.latitude, data.longitude)
                            val markerOptions = MarkerOptions()
                                .position(latLng)
                                .title(data.namaJalan)
                                .snippet("${data.namaUser}, ${data.status}")
                                .icon(vectorToBitmap(R.drawable.baseline_car_crash_24, Color.parseColor("#3DDC84")))

                            val marker = mMap.addMarker(markerOptions)
                            marker?.tag = data
                            boundsBuilder.include(latLng)
                        }
                        val bounds: LatLngBounds = boundsBuilder.build()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                500
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        val dataItem = marker.tag as? DataItem
        if (dataItem != null) {
            Toast.makeText(requireContext(), "Status: ${dataItem.status}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "No data available for this marker", Toast.LENGTH_LONG).show()
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
