package com.example.applicanttrackingsystem.ui.register

import com.example.applicanttrackingsystem.ui.login.LoggedInUserView
import com.example.applicanttrackingsystem.ui.register.RegisterFormState
import com.example.applicanttrackingsystem.ui.register.RegisterResult

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Patterns
import com.example.applicanttrackingsystem.data.LoginRepository
import com.example.applicanttrackingsystem.data.Result

import com.example.applicanttrackingsystem.R

/*  When you turn your phone horizontally, the data will be destroyed if you don't handle it properly
    We create ViewModel to store and manage the ui related data in a lifecycle */

class RegisterViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>() //contains password and login that we typed
    val loginFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val loginResult: LiveData<RegisterResult> = _registerResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _registerResult.value =
                RegisterResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _registerResult.value = RegisterResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.login_invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.login_invalid_password)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
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

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}