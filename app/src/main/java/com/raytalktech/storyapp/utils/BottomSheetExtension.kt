package com.raytalktech.storyapp.utils

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


inline fun <reified T : ViewBinding> AppCompatActivity.showCustomBottomSheet(
    fragmentManager: FragmentManager,
    crossinline bindingInflater: (LayoutInflater) -> T,
    crossinline setup: T.() -> Unit
) {
    val binding = bindingInflater.invoke(layoutInflater)
    val bottomSheet = BottomSheetDialog(this)
    bottomSheet.setContentView(binding.root)
    bottomSheet.setOnShowListener {
        setup.invoke(binding)
    }
    bottomSheet.show()
}


inline fun <reified T : ViewBinding> Fragment.showCustomBottomSheet(
    crossinline bindingInflater: (LayoutInflater) -> T,
    crossinline setup: T.() -> Unit
) {
    val binding = bindingInflater(LayoutInflater.from(context))

    val bottomSheet = BottomSheetDialog(requireContext())
    bottomSheet.setContentView(binding.root)

    setup(binding)

    bottomSheet.show()
}
