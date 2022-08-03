package com.example.applicanttrackingsystem.data

import androidx.lifecycle.MutableLiveData
import com.example.applicanttrackingsystem.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.IOException
import kotlinx.coroutines.tasks.await


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private lateinit var firebaseAuth: FirebaseAuth
    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData<FirebaseUser>()
    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        firebaseAuth = FirebaseAuth.getInstance()

        return try {
            firebaseAuth.signInWithEmailAndPassword(username, password).await()
            userLiveData.postValue(firebaseAuth.currentUser)
            Result.Success(
                LoggedInUser(
                    java.util.UUID.randomUUID().toString(),
                    firebaseAuth.currentUser?.email.toString()
                )
            )
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    suspend fun registerCompany(username: String, password: String): Result<LoggedInUser> {
        firebaseAuth = FirebaseAuth.getInstance()
        return try {
            firebaseAuth.createUserWithEmailAndPassword(username, password).await()
            userLiveData.postValue(firebaseAuth.currentUser)
            Result.Success(
                LoggedInUser(
                    java.util.UUID.randomUUID().toString(),
                    firebaseAuth.currentUser?.email.toString()
                )
            )
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    suspend fun registerFreelancer(username: String, password: String): Result<LoggedInUser> {
        firebaseAuth = FirebaseAuth.getInstance()
        return try {
            firebaseAuth.createUserWithEmailAndPassword(username, password).await()
            userLiveData.postValue(firebaseAuth.currentUser)
            Result.Success(
                LoggedInUser(
                    java.util.UUID.randomUUID().toString(),
                    firebaseAuth.currentUser?.email.toString()
                )
            )
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }
}