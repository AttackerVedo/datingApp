package com.attackervedo.myapplication.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.attackervedo.myapplication.R
import com.attackervedo.myapplication.auth.UserData
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CardStackAdapter(
    val context: Context,
    val itemList : List<UserData>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {
    //화면 연결해주는 부분

        val inflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardStackAdapter.ViewHolder, position: Int) {
    //데이터 넣는 부분
        holder.binding(itemList[position])
    }

    override fun getItemCount(): Int {
    return itemList.size
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(R.id.itemCardImage)
        val nickname = itemView.findViewById<TextView>(R.id.itemCardName)
        val age = itemView.findViewById<TextView>(R.id.itemCardAge)
        val location = itemView.findViewById<TextView>(R.id.itemCardLocation)
        fun binding(data: UserData){
            Log.e("ddddddd", data.uid.toString())
            val storageRef = Firebase.storage.reference.child(data.uid+".png")
            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful){
                    Glide.with(context)
                        .load(task.result)
                        .into(image)
                }
            })
            nickname.text =  data.nickname
            age.text = "${data.age}(${2023-data.age!!.toInt()})"
            location.text = data.location
        }
    }

}