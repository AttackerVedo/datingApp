package com.attackervedo.myapplication

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.attackervedo.myapplication.auth.UserData
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.attackervedo.myapplication.utils.FirebaseRef
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        var uid = FirebaseAuthUtils.getUid()
        getMyDate(uid)

        val mainBackBtn = findViewById<Button>(R.id.myMainBack)
        mainBackBtn.setOnClickListener {
            val intent = Intent(this@MyPageActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getMyDate(uid: String){

        var myImage = findViewById<ImageView>(R.id.myImage)
        var myUid = findViewById<TextView>(R.id.myUid)
        var myNickname = findViewById<TextView>(R.id.myNickname)
        var myGender = findViewById<TextView>(R.id.myGender)
        var myAge = findViewById<TextView>(R.id.myAge)
        var myLocation = findViewById<TextView>(R.id.myLocation)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            var userData = dataSnapshot.getValue(UserData::class.java)

                myUid.text = "MyUid : ${userData?.uid}"
                myNickname.text = "닉네임 : ${userData?.nickname}"
                myGender.text = "성별 : ${userData?.gender}"
                myAge.text = "나이 : ${userData?.age}"
                myLocation.text = "지역 : ${userData?.location}"

                val storageRef = Firebase.storage.reference.child(userData?.uid+".png")
                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if(task.isSuccessful){
                        Glide.with(this@MyPageActivity)
                            .load(task.result)
                            .into(myImage)
                    }
                })

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }
}