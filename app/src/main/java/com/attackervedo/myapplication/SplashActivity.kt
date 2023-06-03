package com.attackervedo.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.attackervedo.myapplication.auth.IntroActivity
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var uid = FirebaseAuthUtils.getUid()
        val handler = Handler(Looper.getMainLooper())
        if( uid == "null"){
            handler.postDelayed({
                val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }else{
            handler.postDelayed({
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            },2000)

        }



    }
}