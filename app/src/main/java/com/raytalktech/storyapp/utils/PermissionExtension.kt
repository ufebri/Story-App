package com.raytalktech.storyapp.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Activity.checkPermission(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Activity.requestPermission(permission: String, requestCode: Int, callback: (Boolean) -> Unit) {
    if (checkPermission(permission)) {
        callback(true)
    } else {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        callback(false)
    }
}

fun Activity.checkAndRequestPermission(permission: String, requestCode: Int, callback: (Boolean) -> Unit) {
    if (checkPermission(permission)) {
        callback(true)
    } else {
        requestPermission(permission, requestCode) { isGranted ->
            callback(isGranted && checkPermission(permission))
        }
    }
}

fun Fragment.checkPermission(permission: String) =
    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.requestPermission(permission: String, requestCode: Int, callback: (Boolean) -> Unit) {
    if (checkPermission(permission)) {
        callback(true)
    } else {
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            callback(isGranted)
        }
        requestPermissionLauncher.launch(permission)
    }
}

fun Fragment.checkAndRequestPermission(permission: String, requestCode: Int, callback: (Boolean) -> Unit) {
    if (checkPermission(permission)) {
        callback(true)
    } else {
        requestPermission(permission, requestCode) { isGranted ->
            callback(isGranted && checkPermission(permission))
        }
    }
}