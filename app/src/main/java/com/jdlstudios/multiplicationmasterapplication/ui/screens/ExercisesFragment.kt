package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModel

class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding
    private val exercisesViewModel: ExercisesViewModel by viewModels()
    private var newListExercises: MutableList<ExerciseUIModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentExercisesBinding.inflate(inflater)

        val args: ExercisesFragmentArgs by navArgs()
        val quantityExercises = args.quantity
        val difficultyExercises = Difficulty.values().getOrElse(args.difficulty) { Difficulty.EASY }

        updateExercisesList(difficultyExercises, quantityExercises)

        exercisesViewModel.selectedPosition.observe(viewLifecycleOwner) {
            starExercise(it)
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

            val correct = exercisesViewModel.exerciseNew.value?.let {
                exercisesViewModel.checkAnswer(
                    answer,
                    it
                )
            }
            correct?.let {
                saveExercise(it)
            }

            exercisesViewModel.nextItem()
            exercisesViewModel.selectedPosition.observe(viewLifecycleOwner) {
                starExercise(it)
            }

            exercisesViewModel.completeExercise()

            binding.quantityExercisesRemainingTextview.text =
                exercisesViewModel.getRemainingExercises().toString()

            correct?.let {
                showUserAnswerResult(it)
            }

            binding.answerEdittext.text.clear()
            Log.i("sum", "ListaNew: $newListExercises")
            exercisesViewModel.stateFinish.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "teminaste!", Toast.LENGTH_SHORT).show()
                    binding.answerEdittext.isEnabled = false
                }
            }
        }
        return binding.root
    }

    private fun starExercise(currentPosition: Int) {
        exercisesViewModel.listExercises.observe(viewLifecycleOwner) {
            binding.exerciseTextview.text = it[currentPosition].toString()
            exercisesViewModel.setExerciseNew(it[currentPosition])
        }
    }

    private fun saveExercise(correct: Boolean) {
        val exerciseNew = exercisesViewModel.exerciseNew.value
        if (exerciseNew != null) {
            exerciseNew.correct = correct
        }
        exerciseNew?.let { newListExercises.add(it) }
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
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.isNotBlank() && answer.toIntOrNull() != null
    }
}