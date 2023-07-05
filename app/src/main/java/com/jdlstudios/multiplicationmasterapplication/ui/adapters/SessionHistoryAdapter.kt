package com.jdlstudios.multiplicationmasterapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class SessionHistoryAdapter : RecyclerView.Adapter<SessionHistoryAdapter.ViewHolder>() {

    var data = listOf<SessionEntity>()

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val answerCorrects: TextView = itemView.findViewById(R.id.correct_session_textview)
        private val answerIncorrects: TextView =
            itemView.findViewById(R.id.incorrect_session_textview)
        private val difficultyText: TextView = itemView.findViewById(R.id.text_session_difficulty)
        private val dateText: TextView = itemView.findViewById(R.id.date_session_textview)
        private val scoreText: TextView = itemView.findViewById(R.id.score_session_textview)
        private val exercisesText: TextView =
            itemView.findViewById(R.id.number_exercises_session_textview)

        fun bind(
            item: SessionEntity
        ) {
            answerCorrects.text = item.correctAnswers.toString()
            answerIncorrects.text = item.incorrectAnswers.toString()
            difficultyText.text = Difficulty.getDifficultyFromInt(item.difficulty).toString()
            dateText.text = item.timestamp.toString()
            scoreText.text = String.format("%d pts", item.score)
            exercisesText.text = String.format("%d exercises", item.numberOfExercises)
        }

        companion object {
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