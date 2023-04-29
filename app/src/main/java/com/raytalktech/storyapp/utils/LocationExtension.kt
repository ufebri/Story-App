package com.raytalktech.storyapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

fun Context.getCurrentLocation(onLocationResult: (Location) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // Handle permissions
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            onLocationResult(it)
        }
    }
}

fun Location.getShortName(context: Context): String? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1) as? MutableList<Address>
        var address: Address? = null
        if (addresses != null && addresses.isNotEmpty()) {
            address = addresses[0]
        }
        address?.adminArea
    } catch (e: IOException) {
        Log.d("TAG", "getShortName: ${e.localizedMessage}")
        null
    }
}