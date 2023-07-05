package com.jdlstudios.multiplicationmasterapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity

class SessionHistoryAdapter(): RecyclerView.Adapter<SessionHistoryAdapter.ViewHolder>(){

    var data = listOf<SessionEntity>()

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        val answerCorrects: TextView = itemView.findViewById(R.id.correct_session_textview)

        fun bind(
            item: SessionEntity
        ){
            answerCorrects.text = item.correctAnswers.toString()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_session, parent, false)
                return ViewHolder(view)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }


}