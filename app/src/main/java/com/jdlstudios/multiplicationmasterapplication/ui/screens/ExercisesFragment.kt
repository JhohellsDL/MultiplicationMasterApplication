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
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModel

class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding
    private val exercisesViewModel: ExercisesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentExercisesBinding.inflate(inflater)

        val args: ExercisesFragmentArgs by navArgs()
        val quantityExercises = args.quantity
        val difficultyExercises = Difficulty.values().getOrElse(args.difficulty) { Difficulty.EASY }

        updateExerciseView(difficultyExercises, quantityExercises)

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
            val correct = exercisesViewModel.checkAnswer(answer)
            val resultExercise = exercisesViewModel.getResultExercise()
            exercisesViewModel.completeExercise()
            binding.quantityExercisesRemainingTextview.text = exercisesViewModel.getRemainingExercises().toString()

            showUserAnswerResult(correct)
            updateExerciseView(difficultyExercises, quantityExercises)

            binding.answerEdittext.text.clear()
        }

        return binding.root
    }

    private fun showUserAnswerResult(correct: Boolean) {
        if (correct) {
            binding.feedbackTextview.text = "Respuesta correcta!"
        } else {
            binding.feedbackTextview.text = "Respuesta incorrecta!"
        }
    }


    private fun updateExerciseView(difficultyExercises: Difficulty, quantity: Int) {
        exercisesViewModel.setExercise(difficultyExercises, quantity)
        exercisesViewModel.setListExercises(difficultyExercises, quantity)
        exercisesViewModel.listExercises.observe(viewLifecycleOwner){
            Log.i("sum","lista_: $it")
        }
        exercisesViewModel.exercise.observe(viewLifecycleOwner) {
            binding.exerciseTextview.text = "${it.factor1} X ${it.factor2} = ${it.answer} -- ${it.correct}"
        }
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.isNotBlank() && answer.toIntOrNull() != null
    }
}