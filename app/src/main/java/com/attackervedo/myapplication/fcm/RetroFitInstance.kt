package com.attackervedo.myapplication.fcm

import com.attackervedo.myapplication.fcm.Repository.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFitInstance {

    companion object{

        private val retrofit by lazy{

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api = retrofit.create(NotiApi::class.java)
    }
}