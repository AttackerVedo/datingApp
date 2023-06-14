package com.attackervedo.myapplication.Adapter

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.attackervedo.myapplication.R

class ListViewAdapter(
    val context: Context,
    val item: MutableList<com.attackervedo.myapplication.auth.UserData>
): BaseAdapter(){
    override fun getCount(): Int {
        return item.size
    }

    override fun getItem(position: Int): Any {
        return item[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.list_view_item,parent,false)
        }

        val nickname = convertView!!.findViewById<TextView>(R.id.listViewNickname)
        nickname.text = item[position].nickname

        return convertView!!
    }
}