package com.raytalktech.storyapp.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raytalktech.storyapp.R
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.data.source.remote.StatusResponse
import com.raytalktech.storyapp.databinding.ActivityLoginBinding
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.UserModel
import com.raytalktech.storyapp.ui.main.MainActivity
import com.raytalktech.storyapp.ui.register.RegisterActivity
import com.raytalktech.storyapp.utils.ViewModelFactory
import com.raytalktech.storyapp.utils.makeClickableSpan

class LoginActivity : AppCompatActivity() {

    //Init Properties
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var launchToRegister: ActivityResultLauncher<Intent>
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        viewModel.getUser().observe(this, {user -> this.user = user})

        binding.apply {
            //Validation Email
            edLoginEmail.doOnTextChanged { text, _, _, _ ->
                binding.tilLoginEmail.let {
                    it.error = if (text.toString().isEmpty()) {
                        it.isErrorEnabled = true
                        getString(R.string.email_error_rules)
                    } else {
                        it.isErrorEnabled = false
                        null
                    }
                }
                validate()
            }

            //Validation Password
            edLoginPassword.doOnTextChanged { _, _, _, _ ->
                binding.tilLoginPassword.let {
                    when (!binding.edLoginPassword.isCharacterPasswordValid) {
                        true -> {
                            it.isErrorEnabled = true
                            it.error = getString(R.string.password_error_rules)
                        }
                        false -> {
                            it.isErrorEnabled = false
                            it.error = null
                        }
                    }
                }
                validate()
            }

            btnLogin.setOnClickListener { submitLogin() }

            tvRegister.text = getString(R.string.text_login_register_now)
            tvRegister.makeClickableSpan("Register", color = Color.BLUE) {
                launchToRegister.launch(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

        // Set Data After Register Success
        launchToRegister =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val mData: Intent? = result.data

                    binding.apply {
                        edLoginEmail.setText(mData?.getStringExtra("email"))
                        edLoginPassword.setText(mData?.getStringExtra("password"))
                    }
                }
            }
    }

    private fun validate() {
        var isValid = true

        //isn't valid if error still show up
        if (binding.edLoginEmail.error != null) isValid = false
        if (binding.edLoginPassword.error != null) isValid = false

        binding.btnLogin.isEnabled = isValid
    }

    private fun submitLogin() {
        showLoading(true)
        viewModel.getLoginData(
            binding.edLoginEmail.text.toString().trim(), binding.edLoginPassword.text.toString()
        ).observe(this, responseData)
    }

    private val responseData = Observer<ApiResponse<DataResponse>> { result ->
        when (result.status) {
            StatusResponse.EMPTY -> {
                Toast.makeText(this, "User Tidak Tersedia", Toast.LENGTH_LONG).show()
                showLoading(false)
            }
            StatusResponse.ERROR -> {
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                showLoading(false)
            }
            StatusResponse.SUCCESS -> {
                showLoading(false)

                binding.apply {
                    viewModel.saveUser(
                        UserModel(
                            result.body.loginResult!!.name,
                            edLoginEmail.text.toString(),
                            edLoginPassword.text.toString(),
                            true,
                            result.body.loginResult!!.token
                        )
                    )
                }

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showLoading(isShowing: Boolean) {
        binding.apply {
            btnLogin.isEnabled = !isShowing
            edLoginEmail.isEnabled = !isShowing
            edLoginPassword.isEnabled = !isShowing

            pbView.visibility = if (isShowing) View.VISIBLE else View.GONE
        }
    }
}