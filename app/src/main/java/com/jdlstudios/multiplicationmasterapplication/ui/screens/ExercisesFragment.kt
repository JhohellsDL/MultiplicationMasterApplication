package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.trace
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.data.models.Session
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.models.ExerciseUIModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModelFactory
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date

class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding
    private var currentSession: SessionEntity? = null
    private var newListExercises: List<Exercise> = mutableListOf()
    private var currentExercise: Exercise? = null
    private var numberExercise: Int = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercisesBinding.inflate(inflater)

// Obtener los argumentos enviados desde la pantalla anterior
        val args: ExercisesFragmentArgs by navArgs()
        val sessionId = args.id
        Log.i("asd", "sessionId Exercices 11: $sessionId")

        //--------------------------------- Para el VIEWMODEL --------------------------------------------------------------
        val application = requireNotNull(this.activity).applicationContext

        val exercisesViewModel: ExercisesViewModel by viewModels {
            ExercisesViewModelFactory((application as MultiplicationApplication).repository)
        }
        //-------------------------------------------------------------------------------------------------------------------

        exercisesViewModel.currentSession.observe(viewLifecycleOwner) {
            currentSession = it
            val currentTimeMillis = it.timestamp
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val dateString = dateFormat.format(Date(currentTimeMillis))
            Log.i("asd", "allSessionn!!! DAte!: $dateString")
            exercisesViewModel.setListExercises(
                difficulty = Difficulty.getDifficultyFromInt(it.difficulty),
                quantity = it.numberOfExercises
            )
        }
        exercisesViewModel.listExercises.observe(viewLifecycleOwner) {
            newListExercises = it
            currentSession?.let {
                binding.difficultyExercisesTextview.text =
                    Difficulty.getDifficultyFromInt(it.difficulty).toString()
                binding.quantityExercisesTextview.text = it.numberOfExercises.toString()
                binding.submitButton.isEnabled = false

                currentExercise = newListExercises[numberExercise]
                binding.quantityExercisesRemainingTextview.text = numberExercise.toString()
                binding.exerciseTextview.text =
                    "${currentExercise!!.operand1} X ${currentExercise!!.operand2} = ${currentExercise!!.answer}"


            }
            Log.i("asd", "newListExercises!!!: $newListExercises")

        }


        binding.submitButton.setOnClickListener {
            numberExercise++
            currentExercise = newListExercises[numberExercise]
            binding.quantityExercisesRemainingTextview.text = numberExercise.toString()
            binding.exerciseTextview.text =
                "${currentExercise!!.operand1} X ${currentExercise!!.operand2} = ${currentExercise!!.answer}"
        }

        binding.answerEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isAnswerValid = isAnswerValid(s.toString())
                binding.submitButton.isEnabled = isAnswerValid
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Observar el ViewModel para saber cuál es el ejercicio seleccionado
        /*exercisesViewModel.selectedPosition.observe(viewLifecycleOwner) {
            starExercise(it)
        }

        // Habilitar el botón de enviar respuesta solamente si el campo de texto no está vacío y contiene un número válido


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
    }*/
        return binding.root
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.isNotBlank() && answer.toIntOrNull() != null
    }
}