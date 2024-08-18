package com.raytalktech.storyapp.ui.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raytalktech.storyapp.R
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.data.source.remote.StatusResponse
import com.raytalktech.storyapp.databinding.ActivityRegisterBinding
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.LoginResult
import com.raytalktech.storyapp.model.UserModel
import com.raytalktech.storyapp.utils.Constants
import com.raytalktech.storyapp.utils.ViewModelFactory
import com.raytalktech.storyapp.utils.makeClickableSpan
import com.raytalktech.storyapp.utils.showAlertOneAction
import com.raytalktech.storyapp.utils.showAlertTwoAction

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        binding.apply {

            //validation name
            edRegisterName.doOnTextChanged { text, _, _, _ ->
                tilRegisterName.let {
                    it.error = if (text.toString().isEmpty()) {
                        it.isErrorEnabled = true
                        getString(R.string.name_error_rules)
                    } else {
                        it.isErrorEnabled = false
                        null
                    }
                }
                validate()
            }

            //Validation Email
            edRegisterEmail.doOnTextChanged { _, _, _, _ -> validate() }

            //Validation Password
            edRegisterPassword.doOnTextChanged { _, _, _, _ -> validate() }

            btnRegister.setOnClickListener { submitRegister() }

            tvLogin.text = getString(R.string.text_register_login_now)
            tvLogin.makeClickableSpan("Login", color = Color.BLUE) {
                finish()
            }
        }
    }

    private fun validate() {
        var isValid = true

        binding.apply {
            //isn't valid if error still show up
            if (edRegisterName.error != null) isValid = false
            if (edRegisterEmail.error != null || edRegisterEmail.text.toString()
                    .isEmpty()
            ) isValid = false
            if (edRegisterPassword.error != null || edRegisterPassword.text.toString()
                    .isEmpty()
            ) isValid = false

            btnRegister.isEnabled = isValid
        }
    }

    private fun submitRegister() {
        showLoading(true)
        binding.apply {
            viewModel.getRegisterData(
                edRegisterName.text.toString(),
                edRegisterEmail.text.toString(),
                edRegisterPassword.text.toString()
            ).observe(this@RegisterActivity, responseData)
        }
    }

    private val responseData = Observer<ApiResponse<DataResponse>> { result ->
        when (result.status) {
            StatusResponse.SUCCESS -> {
                if (!result.body.error) {
                    showAlertTwoAction(result.body.message,
                        "Your account successfully created, login now?",
                        "Login Now",
                        "Later",
                        {
                            val intent = Intent()
                            intent.putExtra("email", binding.edRegisterEmail.text.toString())
                            intent.putExtra("password", binding.edRegisterPassword.text.toString())
                            setResult(RESULT_OK, intent)
                            finish()
                        },
                        { })

                    val loginData: LoginResult? = result.body.loginResult

                    if (loginData != null)
                        saveUser(loginData.token)


                } else {
                    showAlertError(result.body.message)
                }

                showLoading(false)
            }

            StatusResponse.ERROR -> {
                showAlertError(result.body.message)
                showLoading(false)
            }

            StatusResponse.EMPTY -> {
                showAlertError(result.body.message)
                showLoading(false)
            }
        }
    }

    private fun showAlertError(message: String) {
        showAlertOneAction("Oops...", message, "Got it", { })
    }

    private fun showLoading(isShowing: Boolean) {
        binding.apply {
            btnRegister.isEnabled = !isShowing
            edRegisterName.isEnabled = !isShowing
            edRegisterEmail.isEnabled = !isShowing
            edRegisterPassword.isEnabled = !isShowing

            pbView.visibility = if (isShowing) View.VISIBLE else View.GONE
        }
    }

    private fun saveUser(token: String) {
        binding.apply {
            viewModel.saveUser(
                UserModel(
                    edRegisterName.toString(),
                    edRegisterEmail.toString(),
                    edRegisterPassword.toString(),
                    true,
                    token
                )
            )
            Constants.token = token
        }
    }
}