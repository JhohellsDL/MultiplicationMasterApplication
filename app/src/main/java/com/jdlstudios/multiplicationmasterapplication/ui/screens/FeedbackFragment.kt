package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentFeedbackBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.adapters.FeedbackAdapter
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.FeedbackViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.FeedbackViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date

class FeedbackFragment : Fragment() {

    private lateinit var binding: FragmentFeedbackBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(inflater)

        val adapter: FeedbackAdapter = FeedbackAdapter()

        val application = requireNotNull(this.activity).applicationContext
        val feedbackViewModel: FeedbackViewModel by viewModels {
            FeedbackViewModelFactory(
                (application as MultiplicationApplication).sessionRepository,
                application.exerciseRepository
            )
        }

        feedbackViewModel.listExercises.observe(viewLifecycleOwner){
            adapter.data = it
            binding.recyclerView.adapter = adapter
        }

        feedbackViewModel.currentSession.observe(viewLifecycleOwner){
            binding.textDifficulty.text = Difficulty.getDifficultyFromInt(it.difficulty).toString()
            binding.correctTextview.text = it.correctAnswers.toString()
            binding.incorrectTextview.text = it.incorrectAnswers.toString()
            val currentTimeMillis = it.timestamp
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss")
            binding.timeTextview.text = dateFormat.format(Date(currentTimeMillis))
            binding.scoreTextview.text = String.format("%d / 100 pts", it.score)
            binding.progressBarCorrectIncorrect.progress = it.score
            binding.numberExercisesTextview.text = String.format("%d Ejercicios", it.numberOfExercises)
            feedbackViewModel.getListExercises(it.sessionId)
        }

        binding.restartButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_feedbackFragment_to_sessionHistoryFragment)
        }
        return binding.root
    }

    fun sessionToString(session: SessionEntity): String {
        return """
        |Session Details:
        |Difficulty: ${session.sessionId}
        |Difficulty: ${session.difficulty}
        |Number of Exercises: ${session.numberOfExercises}
        |Correct Answers: ${session.correctAnswers}
        |Incorrect Answers: ${session.incorrectAnswers}
        |Score: ${session.score}
        |Timestamp: ${session.timestamp}
    """.trimMargin()
    }
}