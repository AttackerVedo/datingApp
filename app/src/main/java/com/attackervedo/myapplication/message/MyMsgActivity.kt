package com.attackervedo.myapplication.message

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.attackervedo.myapplication.Adapter.ListViewAdapter
import com.attackervedo.myapplication.Adapter.MsgAdapter
import com.attackervedo.myapplication.R
import com.attackervedo.myapplication.auth.UserData
import com.attackervedo.myapplication.utils.FirebaseAuthUtils
import com.attackervedo.myapplication.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyMsgActivity : AppCompatActivity() {

    lateinit var listViewAdapter: MsgAdapter
    val msgList = mutableListOf<MessageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_msg)

        val msgListView = findViewById<ListView>(R.id.msgListView)
        listViewAdapter = MsgAdapter(this,msgList)
        msgListView.adapter = listViewAdapter

        getMyMessage()

    }

    fun getMyMessage(){

        val postListener = object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터 갸져 왔을 때

                msgList.clear()

                for(dataModel in dataSnapshot.children){
                val msg =dataModel.getValue(MessageModel::class.java)
                    msg?.let { msgList.add(it) }
                }
                msgList.reverse()
                listViewAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져 오는거 실패 했을 때

            }
        }
        FirebaseRef.userMsgRef.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)
    }

    }
