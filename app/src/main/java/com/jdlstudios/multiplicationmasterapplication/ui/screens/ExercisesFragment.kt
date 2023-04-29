package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModel

class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding
    private val exercisesViewModel: ExercisesViewModel by viewModels()
    private var listExercises: List<ExerciseUIModel> = listOf()
    private var exerciseNew: ExerciseUIModel = ExerciseUIModel(0, 0, 0, false)
    private var newListExercises: MutableList<ExerciseUIModel> = mutableListOf()
    private var currentPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentExercisesBinding.inflate(inflater)

        val args: ExercisesFragmentArgs by navArgs()
        val quantityExercises = args.quantity
        val difficultyExercises = Difficulty.values().getOrElse(args.difficulty) { Difficulty.EASY }

        updateExercisesList(difficultyExercises, quantityExercises)

        exercisesViewModel.listExercises.observe(viewLifecycleOwner) {
            listExercises = it
        }
        exercisesViewModel.selectedPosition.observe(viewLifecycleOwner) {
            starExercise()
        }

        binding.difficultyExercisesTextview.text = difficultyExercises.name
        binding.quantityExercisesTextview.text = quantityExercises.toString()
        binding.submitButton.isEnabled = false

        binding.answerEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isAnswerValid = isAnswerValid(s.toString())
                binding.submitButton.isEnabled = isAnswerValid
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.submitButton.setOnClickListener {
            val answer = binding.answerEdittext.text.toString().toInt()
            val correct = exercisesViewModel.checkAnswer(answer, exerciseNew)
            saveExercise(correct)

            exercisesViewModel.nextItem()
            exercisesViewModel.selectedPosition.observe(viewLifecycleOwner) {
                currentPosition = it
                starExercise()
            }

            exercisesViewModel.completeExercise()
            binding.quantityExercisesRemainingTextview.text =
                exercisesViewModel.getRemainingExercises().toString()

            showUserAnswerResult(correct)

            binding.answerEdittext.text.clear()
            Log.i("sum", "ListaNew: $newListExercises")
        }
        return binding.root
    }

    private fun starExercise() {
        exerciseNew = listExercises[currentPosition]
        binding.exerciseTextview.text = exerciseNew.toString()
    }

    private fun saveExercise(correct: Boolean) {
        exerciseNew.correct = correct
        newListExercises.add(exerciseNew)
    }

    private fun showUserAnswerResult(correct: Boolean) {
        if (correct) {
            binding.feedbackTextview.text = "Respuesta correcta!"
        } else {
            binding.feedbackTextview.text = "Respuesta incorrecta!"
        }
    }

    private fun updateExercisesList(difficultyExercises: Difficulty, quantity: Int) {
        exercisesViewModel.setListExercises(difficultyExercises, quantity)
        exercisesViewModel.listExercises.observe(viewLifecycleOwner) {
            listExercises = it
        }
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.isNotBlank() && answer.toIntOrNull() != null
    }
}