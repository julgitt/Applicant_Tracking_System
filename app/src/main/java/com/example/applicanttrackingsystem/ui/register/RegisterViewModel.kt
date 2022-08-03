package com.example.applicanttrackingsystem.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applicanttrackingsystem.R
import com.example.applicanttrackingsystem.data.LoginRepository
import com.example.applicanttrackingsystem.data.Result
import com.example.applicanttrackingsystem.ui.login.LoggedInUserView
import java.util.regex.Pattern


/*  When you turn your phone horizontally, the data will be destroyed if you don't handle it properly
    We create ViewModel to store and manage the ui related data in a lifecycle */

class RegisterViewModel(private val loginRepository: LoginRepository) : ViewModel() {


    private val _registerForm = MutableLiveData<RegisterFormState>() //contains data that we typed
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(
        username: String,
        password: String,
        repeatPassword: String,
        nip: String = "none",
        phone: String = "none"
    ) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.register(username, password, repeatPassword, nip, phone)

        if (result is Result.Success) {
            _registerResult.value =
                RegisterResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _registerResult.value = RegisterResult(error = R.string.login_failed)
        }
    }

    fun registerFreelancerDataChanged(username: String, password: String, repeatPassword: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.login_invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.login_invalid_password)
        } else if (!isRepeatPasswordValid(password, repeatPassword)) {
            _registerForm.value =
                RegisterFormState(repeatPasswordError = R.string.login_invalid_repeat_password)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    fun registerCompanyDataChanged(
        username: String,
        password: String,
        repeatPassword: String,
        nip: String,
        phone: String
    ) {
        if (!phoneValid(phone)) {
            _registerForm.value = RegisterFormState(phoneNumberError = R.string.login_invalid_phone)
        } else if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.login_invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.login_invalid_password)
        } else if (!isRepeatPasswordValid(password, repeatPassword)) {
            _registerForm.value =
                RegisterFormState(repeatPasswordError = R.string.login_invalid_repeat_password)
        } else if (!nipValid(nip)) {
            _registerForm.value = RegisterFormState(nipNumberError = R.string.login_invalid_nip)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }
    //TODO: Check if the username or password is already taken

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

    private fun isRepeatPasswordValid(password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }

    private fun phoneValid(phone: String): Boolean {
        if (phone.length < 9) return false
        phone.replace("""[-()+ ]""".toRegex(), "")
        val reg = "^[1-9]\\d{8}\$"
        val pattern: Pattern = Pattern.compile(reg)
        return pattern.matcher(phone).find()
    }

    private fun nipValid(nip: String): Boolean {
        val reg = "^[0-9]{10}\$"
        val pattern: Pattern = Pattern.compile(reg)
        if (!pattern.matcher(nip).find()) {
            return false
        }
        val weight = listOf(6, 5, 7, 2, 3, 4, 5, 6, 7)
        var sum = 0
        val controlNumber = nip.substring(9, 10).toInt()
        var index = 0
        while (index < weight.size) {
            sum += nip[index].digitToInt() * weight[index]
            index++
        }

        return sum % 11 == controlNumber
    }

}