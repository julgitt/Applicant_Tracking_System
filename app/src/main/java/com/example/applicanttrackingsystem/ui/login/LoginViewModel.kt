package com.example.applicanttrackingsystem.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applicanttrackingsystem.R
import com.example.applicanttrackingsystem.data.LoginRepository
import com.example.applicanttrackingsystem.data.Result


/*  When you turn your phone horizontally, the data will be destroyed if you don't handle it properly
    We create ViewModel to store and manage the ui related data in a lifecycle */

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm =
        MutableLiveData<LoginFormState>() //contains password and login that we typed
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        when (val result = loginRepository.login(username, password)) {
            is Result.Success -> {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            }
            else -> {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.login_invalid_username)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

}