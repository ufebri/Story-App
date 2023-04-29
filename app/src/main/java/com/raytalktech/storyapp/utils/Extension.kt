package com.raytalktech.storyapp.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.raytalktech.storyapp.databinding.AlertGeneralOneActionBinding
import com.raytalktech.storyapp.databinding.AlertGeneralTwoActionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

//For Data Store
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

//Make ClickAbleSpan
fun TextView.makeClickableSpan(
    clickableText: String, color: Int = Color.BLUE, onClick: () -> Unit
) {
    val spannable = SpannableString(text)
    val startIndex = text.indexOf(clickableText)
    val endIndex = startIndex + clickableText.length
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = color
        }
    }
    spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = spannable
    movementMethod = LinkMovementMethod.getInstance()
}

fun Context.showAlertTwoAction(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClick: (() -> Unit)? = null,
    onNegativeButtonClick: (() -> Unit)? = null
): AlertDialog {

    val binding = AlertGeneralTwoActionBinding.inflate(LayoutInflater.from(this))
    binding.alertTitle.text = title
    binding.alertDesc.text = message

    val builder = AlertDialog.Builder(this)
    builder.setView(binding.root)
    builder.setCancelable(false)

    val dialog = builder.create()
    dialog.show()

    if (positiveButtonText.isNotEmpty()) {
        binding.btnOk.apply {
            text = positiveButtonText
            setOnClickListener {
                if (onPositiveButtonClick != null) {
                    onPositiveButtonClick()
                    dialog.dismiss()
                }
            }
        }
    }

    if (negativeButtonText.isNotEmpty()) {
        binding.tvCancel.apply {
            text = negativeButtonText
            setOnClickListener {
                if (onNegativeButtonClick != null) onNegativeButtonClick()
                dialog.cancel()
            }
        }
    }

    return dialog
}


fun Context.showAlertOneAction(
    title: String,
    message: String,
    positiveButtonText: String,
    onPositiveButtonClick: ((DialogInterface, Int) -> Unit)? = null
): AlertDialog {

    val binding = AlertGeneralOneActionBinding.inflate(LayoutInflater.from(this))
    binding.alertTitle.text = title
    binding.alertDesc.text = message

    val builder = AlertDialog.Builder(this)
    builder.setView(binding.root)
    builder.setCancelable(false)

    if (positiveButtonText.isNotEmpty()) {
        binding.btnOk.apply {
            text = positiveButtonText
            setOnClickListener { onPositiveButtonClick }
        }
    }

    return builder.create().apply { show() }
}

fun String.toHumanReadable(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.parse(this)
    val now = Date()

    val diffInMillis = now.time - date.time
    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)

    return when {
        diffInMinutes < 1 -> "just now"
        diffInMinutes < 60 -> "$diffInMinutes minutes ago"
        diffInMinutes < 120 -> "an hour ago"
        diffInMinutes < 24 * 60 -> "${diffInMinutes / 60} hours ago"
        diffInMinutes < 48 * 60 -> "yesterday"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.US).format(date)
    }
}