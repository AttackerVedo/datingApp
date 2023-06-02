package com.attackervedo.myapplication.silder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attackervedo.myapplication.R

class CardStackAdapter(
    val context: Context,
    val itemList : List<String>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>(){


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
        fun binding(data: String){

        }
    }

}