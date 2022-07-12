package com.example.applicanttrackingsystem.ui.register

import com.example.applicanttrackingsystem.ui.login.LoggedInUserView


data class RegisterResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)