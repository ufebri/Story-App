package com.raytalktech.storyapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

fun Context.getCurrentLocation(onLocationResult: (Location) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(this)
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Handle permissions
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            onLocationResult(it)
        }
    }
}