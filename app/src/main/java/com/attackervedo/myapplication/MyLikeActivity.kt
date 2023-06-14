package com.attackervedo.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.attackervedo.myapplication.Adapter.ListViewAdapter
import com.attackervedo.myapplication.auth.UserData
import com.attackervedo.myapplication.fcm.NotiModel
import com.attackervedo.myapplication.fcm.PushNotification
import com.attackervedo.myapplication.fcm.RetroFitInstance
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.attackervedo.myapplication.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher


//내가좋아요한 사람들이 나를 좋아요한 리스트를 보여주는 액티비내
class MyLikeActivity : AppCompatActivity() {

    val likeUserListUid = mutableListOf<String>()
    val likeUserList = mutableListOf<UserData>()
    lateinit var userAdapter : ListViewAdapter

    private val uid = FirebaseAuthUtils.getUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like)

        val myLikeUserList = findViewById<ListView>(R.id.myLikeListView)

        userAdapter = ListViewAdapter(this, likeUserList)

        myLikeUserList.adapter = userAdapter

        // 내가 좋아요 한 사람들과 나를 좋아요한 사람의 리스트 받아오기

        //내가좋아요한 사람들 리스트 받아오기
        getMyLikeList()
        // 전체 유저 중에서, 내가 종아요한 사람들을 가져와서 이사람이 나와 매칭이 되어있는지 확인
        myLikeUserList.setOnItemClickListener { parent, view, position, id ->
        checkMatching(likeUserList[position].uid.toString())

            val notiModel = NotiModel("이씨발럼이","내성격 까먹었나보네?")

            val pushModel = PushNotification(notiModel,likeUserList[position].token.toString())

            testPush(pushModel)

        }
    }//onCreate

    fun checkMatching(clickedUid:String){
        val postListener = object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.children.count() == 0){
                    Toast.makeText(this@MyLikeActivity, "매칭이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    for(dataModel in dataSnapshot.children){
                        val likeUserKey = dataModel.key.toString()
                        if(likeUserKey.equals(uid))
                            Toast.makeText(this@MyLikeActivity, "매칭이 되었습니다.", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this@MyLikeActivity, "매칭이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져 오는거 실패 했을 때
            }
        }
        FirebaseRef.userLikeRef.child(clickedUid).addValueEventListener(postListener)
    }


    //내가좋아요한 사람들 리스트 받아오기
    fun getMyLikeList(){
        val postListener = object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children){
                likeUserListUid.add(dataModel.key.toString())
                }
                getUserList()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져 오는거 실패 했을 때
            }
        }
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)
    }
//전체 유저 데이터 받아오기
    fun getUserList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터 갸져 왔을 때
                for(dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserData::class.java)
                    if(likeUserListUid.contains(user?.uid.toString())){
                        //내가 좋아요한 사람들의 정보만 뽑아 올 수 있음.
                        // 전체 유저중에 내가 좋아요한 사람들의 정보만 add함
                        user?.let { likeUserList.add(it) }
                    }

                }
                userAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져 오는거 실패 했을 때
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }
    //push
    private fun testPush(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        RetroFitInstance.api.postNotification(notification)

    }
}