package com.example.applicanttrackingsystem.ui.register

data class RegisterFormState (
    val usernameError: Int? = null,
    val phoneNumberError: Int? = null,
    val nipNumberError: Int? = null,
    val firstNameError: Int? = null,
    val secondNameError: Int? = null,
    val passwordError: Int? = null,
    val repeatPasswordError: Int? = null,
    val isDataValid: Boolean = false
)