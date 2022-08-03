package com.example.applicanttrackingsystem.data

import com.example.applicanttrackingsystem.data.model.LoggedInUser
import kotlinx.coroutines.runBlocking

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {


    // in-memory cache of the loggedInUser object
    private var user: LoggedInUser? = null

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> = runBlocking {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return@runBlocking result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    fun register(
        username: String,
        password: String,
        repeatPassword: String,
        nip: String,
        phone: String
    ) = runBlocking {
        val result = if (nip != "none") dataSource.registerCompany(username, password)
        else dataSource.registerFreelancer(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return@runBlocking result
    }

}