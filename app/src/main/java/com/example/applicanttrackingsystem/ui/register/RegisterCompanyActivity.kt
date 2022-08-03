package com.example.applicanttrackingsystem.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.applicanttrackingsystem.CompanyActivity
import com.example.applicanttrackingsystem.R
import com.example.applicanttrackingsystem.databinding.ActivityCompanyRegisterBinding
import com.example.applicanttrackingsystem.ui.login.LoggedInUserView
import com.example.applicanttrackingsystem.ui.login.afterTextChanged

class RegisterCompanyActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityCompanyRegisterBinding
    private lateinit var newActivity: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phone = binding.registerPhoneNumber
        val nip = binding.registerNipNumber
        val password = binding.registerPassword
        val email = binding.registerUsername
        val repeatPassword = binding.registerRepeatPassword
        val registerButton = binding.registerButton
        val loading = binding.registerLoading

        newActivity = Intent(this, CompanyActivity::class.java)
        registerViewModel =
            ViewModelProvider(this, RegisterViewModelFactory())[RegisterViewModel::class.java]
        registerViewModel.registerFormState.observe(this@RegisterCompanyActivity, Observer {
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
            if (registerState.nipNumberError != null) {
                nip.error = getString(registerState.nipNumberError)
            }
            if (registerState.phoneNumberError != null) {
                phone.error = getString(registerState.phoneNumberError)
            }

        })

        registerViewModel.registerResult.observe(this@RegisterCompanyActivity, Observer {
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

        phone.afterTextChanged {
            registerViewModel.registerCompanyDataChanged(
                email.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString(),
                nip.text.toString(),
                phone.text.toString()
            )
        }
        email.afterTextChanged {
            registerViewModel.registerCompanyDataChanged(
                email.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString(),
                nip.text.toString(),
                phone.text.toString()
            )
        }
        password.afterTextChanged {
            registerViewModel.registerCompanyDataChanged(
                email.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString(),
                nip.text.toString(),
                phone.text.toString()
            )
        }
        repeatPassword.afterTextChanged {
            registerViewModel.registerCompanyDataChanged(
                email.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString(),
                nip.text.toString(),
                phone.text.toString()
            )
        }

        nip.apply {
            afterTextChanged {
                registerViewModel.registerCompanyDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    repeatPassword.text.toString(),
                    nip.text.toString(),
                    phone.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            email.text.toString(),
                            password.text.toString(),
                            repeatPassword.text.toString(),
                            nip.text.toString(),
                            phone.text.toString()
                        )
                }
                false
            }

            registerButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                registerViewModel.register(
                    email.text.toString(),
                    password.text.toString(),
                    repeatPassword.text.toString(),
                    nip.text.toString(),
                    phone.text.toString()
                )
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
        startActivity(newActivity)
    }

    private fun showRegisterFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}


