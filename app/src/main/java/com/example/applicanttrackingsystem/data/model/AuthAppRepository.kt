package com.example.applicanttrackingsystem.data.model

import android.app.Activity
import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthAppRepository : Activity() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun login(email: String, password: String) {
        var result: Boolean = false
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)
                    result = true
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
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
