package com.raytalktech.storyapp.customui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {

    var isCharacterPasswordValid = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
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
                    isCharacterPasswordValid = p0.toString().length > 7
            }

            override fun afterTextChanged(p0: Editable?) {
                //do nothing
            }

        })
    }
}