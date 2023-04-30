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

        // Obtener los argumentos enviados desde la pantalla anterior
        val args: ExercisesFragmentArgs by navArgs()
        val quantityExercises = args.quantity
        val difficultyExercises = Difficulty.values().getOrElse(args.difficulty) { Difficulty.EASY }

        // Actualizar la lista de ejercicios con la dificultad y cantidad de ejercicios recibidos
        updateExercisesList(difficultyExercises, quantityExercises)

        // Observar el ViewModel para saber cuál es el ejercicio seleccionado
        exercisesViewModel.selectedPosition.observe(viewLifecycleOwner) {
            starExercise(it)
        }

        // Mostrar la dificultad y cantidad de ejercicios en la pantalla
        binding.difficultyExercisesTextview.text = difficultyExercises.name
        binding.quantityExercisesTextview.text = quantityExercises.toString()
        binding.submitButton.isEnabled = false

        // Habilitar el botón de enviar respuesta solamente si el campo de texto no está vacío y contiene un número válido
        binding.answerEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isAnswerValid = isAnswerValid(s.toString())
                binding.submitButton.isEnabled = isAnswerValid
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Acción al presionar el botón de enviar respuesta
        binding.submitButton.setOnClickListener {

            // Obtener la respuesta ingresada por el usuario
            val answer = binding.answerEdittext.text.toString().toInt()

            // Verificar si la respuesta es correcta y almacenar el ejercicio en la lista de ejercicios realizados
            val correct = exercisesViewModel.exerciseNew.value?.let {
                exercisesViewModel.checkAnswer(
                    answer,
                    it
                )
            }
            correct?.let {
                saveExercise(it)
            }

            // Ir al siguiente ejercicio y actualizar la pantalla
            exercisesViewModel.nextItem()
            exercisesViewModel.selectedPosition.observe(viewLifecycleOwner) {
                starExercise(it)
            }

            // Verificar si ya se completaron todos los ejercicios y mostrar un mensaje de finalización
            exercisesViewModel.completeExercise()
            binding.quantityExercisesRemainingTextview.text =
                exercisesViewModel.getRemainingExercises().toString()

            // Mostrar el resultado de la respuesta del usuario
            correct?.let {
                showUserAnswerResult(it)
            }

            // Limpiar el campo de texto y verificar si se completaron todos los ejercicios
            binding.answerEdittext.text.clear()
            Log.i("sum", "ListaNew: $newListExercises")
            exercisesViewModel.stateFinish.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "¡Terminaste!", Toast.LENGTH_SHORT).show()
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