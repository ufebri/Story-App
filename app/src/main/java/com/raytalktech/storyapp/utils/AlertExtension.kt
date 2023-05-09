package com.raytalktech.storyapp.utils

import android.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.showAlert(
    title: String,
    message: String,
    positiveButtonTitle: String = "OK",
    negativeButtonTitle: String? = null,
    positiveAction: (() -> Unit)? = null,
    negativeAction: (() -> Unit)? = null
) {
    val builder = AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonTitle) { dialog, _ ->
            positiveAction?.invoke()
            dialog.dismiss()
        }

    negativeButtonTitle?.let {
        builder.setNegativeButton(it) { dialog, _ ->
            negativeAction?.invoke()
            dialog.dismiss()
        }
    }

    builder.create().show()
}