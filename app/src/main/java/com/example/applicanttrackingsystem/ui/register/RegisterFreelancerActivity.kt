package com.example.applicanttrackingsystem.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.applicanttrackingsystem.R
import com.example.applicanttrackingsystem.databinding.ActivityCompanyRegisterBinding
import com.example.applicanttrackingsystem.databinding.ActivityFreelancerRegisterBinding
import com.example.applicanttrackingsystem.databinding.ActivityLoginBinding
import com.example.applicanttrackingsystem.ui.login.LoggedInUserView
import com.example.applicanttrackingsystem.ui.login.LoginViewModel
import com.example.applicanttrackingsystem.ui.login.afterTextChanged

class RegisterFreelancerActivity: AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityFreelancerRegisterBinding
    private lateinit var freelancerActivity : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreelancerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.registerUsername
        val password = binding.registerPassword
        val repeatPassword = binding.registerRepeatPassword
        val registerButton = binding.registerButton
        val loading = binding.registerLoading

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterFreelancerActivity, Observer {
            val registerState = it ?: return@Observer

            // disable register button unless data is valid
            registerButton.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                email.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
            if (registerState.repeatPasswordError != null) {
                repeatPassword.error = getString(registerState.repeatPasswordError)
            }
        })

        registerViewModel.registerResult.observe(this@RegisterFreelancerActivity, Observer {
            val registerResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (registerResult.error != null) {
                showRegisterFailed(registerResult.error)
            }
            if (registerResult.success != null) {
                updateUiWithUser(registerResult.success)
            }
            setResult(RESULT_OK)



            //Complete and destroy login activity once successful
            finish()
        })
        email.afterTextChanged {
            registerViewModel.registerFreelancerDataChanged(
                email.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString(),
            )
        }
        password.afterTextChanged {
            registerViewModel.registerFreelancerDataChanged(
                email.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString(),
            )
        }

        repeatPassword.apply {
            afterTextChanged {
                registerViewModel.registerFreelancerDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    repeatPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            email.text.toString(),
                            password.text.toString(),
                        )
                }
                false
            }

            registerButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                registerViewModel.register(email.text.toString(), password.text.toString())
            }

        }
    }
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.login_welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showRegisterFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

