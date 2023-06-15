package com.attackervedo.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.attackervedo.myapplication.R
import com.attackervedo.myapplication.message.MessageModel
import org.w3c.dom.Text

class MsgAdapter(
    val context: Context,
    val item: MutableList<MessageModel>
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

        val nicknameArea = convertView!!.findViewById<TextView>(R.id.nick)
        val textArea = convertView!!.findViewById<TextView>(R.id.listViewNickname)
        nicknameArea.text = item[position].sendInfo
        textArea.text = item[position].sendText

        return convertView!!
    }
}