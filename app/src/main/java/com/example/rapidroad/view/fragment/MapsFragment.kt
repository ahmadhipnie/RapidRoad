package com.example.rapidroad.view.fragment

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rapidroad.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker nang omahku"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap.uiSettings.isZoomControlsEnabled = true

        // Enable zoom gestures
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true

        // Enable compass
        googleMap.uiSettings.isCompassEnabled = true

        // Enable My Location Button
        googleMap.uiSettings.isMapToolbarEnabled = true

        googleMap.uiSettings.setAllGesturesEnabled(true)

        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }
}