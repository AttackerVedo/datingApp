package com.attackervedo.myapplication.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.attackervedo.myapplication.MainActivity
import com.attackervedo.myapplication.R
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginLoginBtn)
        val backBtn = findViewById<Button>(R.id.loginBackBtn)
        //back button go to intro Activity
        backBtn.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
        //login button -> go login
        loginBtn.setOnClickListener {

            val email = findViewById<TextInputEditText>(R.id.loginEmail)
            val password = findViewById<TextInputEditText>(R.id.loginPassword)

            auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException) {
                            // Invalid email (user not found)
                            Toast.makeText(
                                baseContext,
                                "존재하지 않는 아이디 입니다 회원가입을 해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (exception is FirebaseAuthInvalidCredentialsException) {
                            // Invalid password
                            Toast.makeText(
                                baseContext,
                                "비밀번호가 틀렸습니다. 다시한번 입력해 주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
        }



    }//onCreate
}