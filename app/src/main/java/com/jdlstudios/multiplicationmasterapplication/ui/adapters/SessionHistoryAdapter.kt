package com.jdlstudios.multiplicationmasterapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar_session_correct_incorrect)
        private val cardView: MaterialCardView = itemView.findViewById(R.id.card_view_detail)
        private val fondo: ConstraintLayout = itemView.findViewById(R.id.constraint_view_card)

        fun bind(
            item: SessionEntity
        ) {
            if (item.correctAnswers == 0 && item.incorrectAnswers == 0){
                cardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.accent5)
                answerCorrects.text = item.correctAnswers.toString()
                answerIncorrects.text = item.incorrectAnswers.toString()
                difficultyText.text = R.string.sesion_incompleta.toString()
                progressBar.progress = 0
                scoreText.text = String.format("%d / 100 pts", 0)
                exercisesText.text = String.format("%d exercises", item.numberOfExercises)

                val currentTimeMillis = item.timestamp
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
                val dateString = dateFormat.format(Date(currentTimeMillis))

                dateText.text = dateString
            } else if (item.incorrectAnswers != 0){
                cardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.accent3)
                answerCorrects.text = item.correctAnswers.toString()
                answerIncorrects.text = item.incorrectAnswers.toString()
                difficultyText.text = Difficulty.getDifficultyFromInt(item.difficulty).toString()
                progressBar.progress = 0
                scoreText.text = String.format("%d / 100 pts", 0)
                exercisesText.text = String.format("%d exercises", item.numberOfExercises)

                val currentTimeMillis = item.timestamp
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
                val dateString = dateFormat.format(Date(currentTimeMillis))

                dateText.text = dateString
            } else {
                cardView.setStrokeColor(ContextCompat.getColor(itemView.context, R.color.colorTextSecondary2))
                answerCorrects.text = item.correctAnswers.toString()
                answerIncorrects.text = item.incorrectAnswers.toString()
                difficultyText.text = Difficulty.getDifficultyFromInt(item.difficulty).toString()
                progressBar.progress = item.score
                scoreText.text = String.format("%d / 100 pts", item.score)
                exercisesText.text = String.format("%d exercises", item.numberOfExercises)

                val currentTimeMillis = item.timestamp
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
                val dateString = dateFormat.format(Date(currentTimeMillis))

                dateText.text = dateString
            }
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