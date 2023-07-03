package com.jdlstudios.multiplicationmasterapplication.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.ui.screens.FeedbackFragment

class FeedbackAdapter(): RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {

    var data = listOf<Exercise>()

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val answerUser: TextView = itemView.findViewById(R.id.text_answer_user)
        val answer: TextView = itemView.findViewById(R.id.text_answer)
        val isCorrect: TextView = itemView.findViewById(R.id.text_correct)
        val exercise: TextView = itemView.findViewById(R.id.text_exercise_complete)
        val image: ImageView = itemView.findViewById(R.id.image_reading_item_exercise)

        fun bind(
            item: Exercise
        ){
            answerUser.text = item.answerUser.toString()
            answer.text = item.answer.toString()
            if (item.correct){
                isCorrect.text = "Correcto"
                image.setImageResource(R.drawable.baseline_good_green)
            }else{
                isCorrect.text = "Incorrecto"
                image.setImageResource(R.drawable.baseline_bad_red)
            }
            exercise.text = "${item.operand1} X ${item.operand2}"
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