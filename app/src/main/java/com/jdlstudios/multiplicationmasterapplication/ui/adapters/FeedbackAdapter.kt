package com.jdlstudios.multiplicationmasterapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise

class FeedbackAdapter(): RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {

    var data = listOf<Exercise>()

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val answerUser: TextView = itemView.findViewById(R.id.text_answer_user)

        fun bind(
            item: Exercise
        ){
            answerUser.text = item.answerUser.toString()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_exercise, parent, false)
                return ViewHolder(view)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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