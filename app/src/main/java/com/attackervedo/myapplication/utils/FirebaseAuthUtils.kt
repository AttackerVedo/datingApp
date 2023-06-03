package com.attackervedo.myapplication.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthUtils {

    companion object{

        private lateinit var auth: FirebaseAuth

        fun getUid():String{
            auth = FirebaseAuth.getInstance()
            var currentUserUid = auth.currentUser?.uid.toString()
            return currentUserUid
        }
    }
}