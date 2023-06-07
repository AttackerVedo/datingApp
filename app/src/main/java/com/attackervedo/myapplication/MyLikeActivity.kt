package com.attackervedo.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.attackervedo.myapplication.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


//내가좋아요한 사람들이 나를 좋아요한 리스트를 보여주는 액티비내
class MyLikeActivity : AppCompatActivity() {

    private val uid = FirebaseAuthUtils.getUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like)

        // 내가 좋아요 한 사람들과 나를 좋아요한 사람의 리스트 받아오기

        //내가좋아요한 사람들 리스트 받아오기
        getMyLikeList()

    }
    //내가좋아요한 사람들 리스트 받아오기
    fun getMyLikeList(){
        val postListener = object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children){

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져 오는거 실패 했을 때
            }
        }
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)
    }
}