package com.example.applicanttrackingsystem.data.model

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthAppRepository(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.P)
    fun login(email: String?, password: String?) {
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(
                application.mainExecutor
            ) { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        application.applicationContext,
                        "Login Failure: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun register(email: String?, password: String?) {
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(
                application.mainExecutor
            ) { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        application.applicationContext,
                        "Registration Failure: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    init {
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }
}