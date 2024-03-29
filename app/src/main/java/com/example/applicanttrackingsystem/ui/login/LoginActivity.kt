package com.example.applicanttrackingsystem.ui.login

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
import com.example.applicanttrackingsystem.CompanyActivity
import com.example.applicanttrackingsystem.TypeChoiceActivity
import com.example.applicanttrackingsystem.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    //private lateinit var bundle: Bundle
    //private lateinit var type: String
    //private lateinit var companyActivity: Intent
    //private lateinit var freelancerActivity: Intent
    private lateinit var userActivity: Intent
    private lateinit var registerActivity: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //bundle = intent.extras!!
        //type = bundle.getString("type").toString()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.loginUsername
        val password = binding.loginPassword
        val loginButton = binding.loginButton
        val registerButton = binding.loginRegisterButton
        //TODO:Forgot password functionality
        val forgotPassword = binding.loginForgotPasswordButton
        val loading = binding.loginLoading

        //companyActivity = Intent(this, RegisterCompanyActivity::class.java)
        //freelancerActivity = Intent(this, RegisterFreelancerActivity::class.java)
        userActivity = Intent(this, CompanyActivity::class.java)
        registerActivity = Intent(this, TypeChoiceActivity::class.java)
        

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        //observer provides us the current value of a LiveData
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            loginButton.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }

            setResult(RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            loginButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
        registerButton.setOnClickListener {
            startActivity(registerActivity)
           /* when (intent.getStringExtra("type")!!) {
                "company" -> {
                    startActivity(companyActivity)
                }
                "freelancer" -> {
                    startActivity(freelancerActivity)
                }
                else -> {
                    throw Exception("Something went wrong")
                }
            }*/
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(com.example.applicanttrackingsystem.R.string.login_welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        startActivity(userActivity)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}