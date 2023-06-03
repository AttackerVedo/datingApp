package com.attackervedo.myapplication.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.attackervedo.myapplication.R
import kotlin.math.log

class IntroActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val joinBtn = findViewById<Button>(R.id.introJoinBtn)
        joinBtn.setOnClickListener {
            val intent = Intent(this@IntroActivity, JoinActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginBtn = findViewById<Button>(R.id.introLoginBtn)
        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}