package com.attackervedo.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.attackervedo.myapplication.Adapter.ListViewAdapter
import com.attackervedo.myapplication.auth.UserData
import com.attackervedo.myapplication.fcm.NotiModel
import com.attackervedo.myapplication.fcm.PushNotification
import com.attackervedo.myapplication.fcm.RetroFitInstance
import com.attackervedo.myapplication.message.MessageModel
import com.attackervedo.myapplication.message.MyMsgActivity
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.attackervedo.myapplication.utils.FirebaseRef
import com.attackervedo.myapplication.utils.MyInfo
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
    lateinit var token : String


    private val uid = FirebaseAuthUtils.getUid()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like)

        val showMsgBtn = findViewById<Button>(R.id.showMsgBtn)
        showMsgBtn.setOnClickListener {

            val intent = Intent(this, MyMsgActivity::class.java)
            startActivity(intent)

        }


        val myLikeUserList = findViewById<ListView>(R.id.myLikeListView)

        userAdapter = ListViewAdapter(this, likeUserList)

        myLikeUserList.adapter = userAdapter

        // 내가 좋아요 한 사람들과 나를 좋아요한 사람의 리스트 받아오기

        //내가좋아요한 사람들 리스트 받아오기
        getMyLikeList()
        // 전체 유저 중에서, 내가 종아요한 사람들을 가져와서 이사람이 나와 매칭이 되어있는지 확인
//        myLikeUserList.setOnItemClickListener { parent, view, position, id ->
//        checkMatching(likeUserList[position].uid.toString())
//
//            val notiModel = NotiModel("이씨발럼이","내성격 까먹었나보네?")
//
//            val pushModel = PushNotification(notiModel,likeUserList[position].token.toString())
//
//            testPush(pushModel)
//
//        }
        myLikeUserList.setOnItemLongClickListener { parent, view, position, id ->

            checkMatching(likeUserList[position].uid.toString())
            token = likeUserList[position].token.toString()


            return@setOnItemLongClickListener(true)
        }


    }//onCreate

    //내가 좋아요한 유저를 클릭하면은 메세지보내기 창이 떠서 메세지를 보낼수 있게하고
    //메세지 보내고 상대바에게 push알람 띄워주고
    //만약에 서로좋아요 하지않은 사람이라면 메세지 못보냄


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
                        {
                            Toast.makeText(this@MyLikeActivity, "매칭이 되었습니다.", Toast.LENGTH_SHORT).show()
                            //Dialog
                            showDialog(clickedUid)
                        }
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

    //Dialog
    fun showDialog(clickedUid:String){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("메세지 보내기")

        val mAlertDialog = mBuilder.show()

        val sendBtn = mAlertDialog.findViewById<Button>(R.id.sendBtn)
        val sendTextArea = mAlertDialog.findViewById<EditText>(R.id.sendTextArea)
        sendBtn!!.setOnClickListener {

            val msgModel = MessageModel(
                MyInfo.myNickname,
                sendTextArea!!.text.toString()
            )

            FirebaseRef.userMsgRef.child(clickedUid).push().setValue(msgModel)

            val notiModel = NotiModel(MyInfo.myNickname,sendTextArea.text.toString())

            val pushModel = PushNotification(notiModel,token)

            testPush(pushModel)


            mAlertDialog.dismiss()
            Toast.makeText(this, "메세지 전송 완료", Toast.LENGTH_SHORT).show()

        }
    }



}