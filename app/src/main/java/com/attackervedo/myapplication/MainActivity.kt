package com.attackervedo.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.attackervedo.myapplication.auth.IntroActivity
import com.attackervedo.myapplication.auth.UserData
import com.attackervedo.myapplication.Adapter.CardStackAdapter
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.attackervedo.myapplication.utils.FirebaseRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

    val userList = mutableListOf<UserData>()
    var userCount = 0
    var uid = FirebaseAuthUtils.getUid()
    lateinit var currentUserGender : String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object:CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
            // drag process

                if(direction == Direction.Right){
                    // drag for right
                    var otherUid :String = userList[userCount].uid.toString()
                    var userName = userList[userCount].nickname.toString()
                    userLikeOtherUser(uid, otherUid,userName)
//                    createNotificationChannel()
//                    sendNotification(userName)

                }

                if(direction == Direction.Left){
                    // drag for left

                }

                userCount += 1
                if(userCount == userList.count()){
                    getUserDataList(currentUserGender)
                }
            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        cardStackAdapter = CardStackAdapter(baseContext, userList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter
        //logout
        val logoutBtn = findViewById<Button>(R.id.mainLogout)
        logoutBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
        //myPage
        val myPageBtn = findViewById<Button>(R.id.mainMyPage)
        myPageBtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
            finish()
        }
//        getUserDataList()
        // get user that different gender
        getMyData()

        val likeBtn = findViewById<Button>(R.id.mainLikeBtn)
        likeBtn.setOnClickListener {
            val intent = Intent(this@MainActivity , MyLikeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }//onCreate

    // 내가 좋아요 한 사람 리스트
    // 내가 좋아요 한 사람이 좋아요 한 사람 리스트

    // 1. 내 성별 알기
    // 2. 전체 유저 중에서 나의 성별과 다른 사람을 가져오기

    // get my data
    fun getMyData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userData = dataSnapshot.getValue(UserData::class.java)
                currentUserGender = userData?.gender.toString()
                getUserDataList(currentUserGender)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    fun getUserDataList(currentUserGender:String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            // 데이터 갸져 왔을 때

          for(dataModel in dataSnapshot.children){
//              Log.e("dataModel", dataModel.toString())
              val user = dataModel.getValue(UserData::class.java)

              if(user?.gender.toString().equals(currentUserGender)){

              }
              else
              {
              user?.let { userList.add(it) }
              }

          }
                cardStackAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
    // 데이터 가져 오는거 실패 했을 때
    // Getting Post failed, log a message
    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    // 유저의 좋아요를 표시하는부분
    // 데이터에서 값을 지정해야하는데, 어떤값을 저장할까...?
    // 나의 uid, 좋아요한 사람의 uid
    fun userLikeOtherUser(myUid:String, otherUid : String,userName: String){
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")
        getOtherUserLikeList(otherUid,userName)
    }

    //내가 좋아요한 사람이 누구를 좋아요 했는지 알 수 있음.
    fun getOtherUserLikeList(otherUid: String,userName: String){
        val postListener = object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터 가져 왔을 때
                // 여기 리스트 안에서 나의 Uid 가 있는 지 확인만 해주면 됨.
                for(dataModel in dataSnapshot.children){
                val likeUserKey = dataModel.key.toString()
                    if(likeUserKey.equals(uid)){
                        Toast.makeText(this@MainActivity, "매칭 완료", Toast.LENGTH_SHORT).show()
                        createNotificationChannel()
                        sendNotification(userName)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져 오는거 실패 했을 때
            }
        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    //Notification

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(userName: String){
        var builder = NotificationCompat.Builder(this, "Test_Channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("매칭완료")
            .setContentText("매칭이 완료되었습니다 ${userName} 님도 나를 좋아해요.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123,builder.build())
        }
    }

    // 1. 앱에서 코드로 notification 띄우기
    // 2. firebase console에서 모든앱 사용자에게 push 보내기
    // 3. 특정사용자에게 메세지 보내기(firebase console에서)
    // 4. firebase console 이 아니라, 앱에서 직접 다른 사람에게 푸시 메세지 보내기

}