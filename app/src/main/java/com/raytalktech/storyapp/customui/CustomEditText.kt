package com.raytalktech.storyapp.customui

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {

    var isCharacterPasswordValid = false
    var isEmailFormatValid = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                if (p0.toString().isNotEmpty())
                    when (inputType) {
                        InputType.TYPE_TEXT_VARIATION_PASSWORD, 129 -> {
                            isCharacterPasswordValid =
                                p0.toString().length > 7

                            error = if (isCharacterPasswordValid) null else "Minimum 8 Character"
                        }
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, 33 -> {
                            isEmailFormatValid =
                                Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}").matches(p0.toString())

                            error = if (isEmailFormatValid) null else "Email Format Invalid"
                        }
                        else -> Log.d("apanih", "onTextChanged: $inputType")
                    }
            }

            override fun afterTextChanged(p0: Editable?) {
                //do nothing
            }
        })
    }
}