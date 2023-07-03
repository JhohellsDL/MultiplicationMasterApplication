package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModelFactory

class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding
    private var currentSession: SessionEntity? = null
    private var newListExercises: List<Exercise> = mutableListOf()

    private lateinit var exerciseForAdd: Exercise
    private var sessionId: Long = 0
    private var currentExercise: Exercise? = null

    private var numberTotalExercise: Int = 0
    private var numberExercise: Int = 0
    private var numberCorrects: Int = 0
    private var numberIncorrects: Int = 0
    private var answerUser: Int = 0
    private var isCorrect: Boolean = false

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercisesBinding.inflate(inflater)

        val application = requireNotNull(this.activity).applicationContext
        val exercisesViewModel: ExercisesViewModel by viewModels {
            ExercisesViewModelFactory(
                (application as MultiplicationApplication).sessionRepository,
                application.exerciseRepository
            )
        }

        exercisesViewModel.currentSession.observe(viewLifecycleOwner) {
            currentSession = it
            exercisesViewModel.setListExercises(
                difficulty = Difficulty.getDifficultyFromInt(it.difficulty),
                quantity = it.numberOfExercises
            )
        }

        exercisesViewModel.listExercises.observe(viewLifecycleOwner) { listExercises ->
            newListExercises = listExercises
            numberTotalExercise = listExercises.size
            currentSession?.let {
                binding.difficultyExercisesTextview.text =
                    Difficulty.getDifficultyFromInt(it.difficulty).toString()
                binding.quantityExercisesTextview.text = it.numberOfExercises.toString()
                binding.submitButton2.isVisible = false
                sessionId = currentSession!!.sessionId

                currentExercise = newListExercises[numberExercise]
                binding.quantityExercisesRemainingTextview.text = numberExercise.toString()
                binding.exerciseTextview.text = currentExercise!!.operand1.toString()
                binding.exerciseTextview2.text = currentExercise!!.operand2.toString()

            }

            Log.i("asd", "newListExercises!!!: $newListExercises")

        }

        binding.submitButton2.setOnClickListener {
            answerUser = binding.answerEdittext.text.toString().toInt()
            val answer = currentExercise!!.answer
            if (answer == answerUser) {
                isCorrect = true
                numberCorrects++
            }else{
                isCorrect = false
                numberIncorrects++
            }

            exerciseForAdd = Exercise(
                sessionId = sessionId,
                operand1 = currentExercise!!.operand1,
                operand2 = currentExercise!!.operand2,
                answer = currentExercise!!.answer,
                answerUser = answerUser,
                correct = isCorrect
            )
            Log.i("asd", "Exercise for add: ${getExerciseString(exerciseForAdd)}")
            exercisesViewModel.exerciseForAdd2(exerciseForAdd)
            exercisesViewModel.insertExercise()

            if (numberExercise == newListExercises.size - 1) {
                exercisesViewModel.sessionForUpdate(numberCorrects, numberIncorrects, getScore(numberTotalExercise, numberCorrects))
                exercisesViewModel.updateSession()
                it.findNavController().navigate(R.id.action_exercisesFragment_to_feedbackFragment)
            } else {
                numberExercise++
                Log.i("asd", "Exercise for add: $numberExercise - ${newListExercises.size}")
            }

            currentExercise = newListExercises[numberExercise]
            binding.quantityExercisesRemainingTextview.text = numberExercise.toString()
            binding.exerciseTextview.text = currentExercise!!.operand1.toString()
            binding.exerciseTextview2.text = currentExercise!!.operand2.toString()
        }

        binding.answerEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isAnswerValid = isAnswerValid(s.toString())
                binding.submitButton2.isVisible = isAnswerValid
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

    private fun getScore(numberTotalExercise: Int, numberCorrects: Int): Int {
        var score: Int = 0
        score = (100*numberCorrects)/numberTotalExercise
        return score
    }

    fun getExerciseString(exercise: Exercise): String {
        val sessionId = exercise.sessionId ?: "null"
        val operand1 = exercise.operand1 ?: 0
        val operand2 = exercise.operand2 ?: 0
        val answer = exercise.answer ?: 0
        val answerUser = exercise.answerUser ?: 0
        val correct = exercise.correct ?: false

        return "sessionId: $sessionId, " +
                "operand1: $operand1, " +
                "operand2: $operand2, " +
                "answer: $answer, " +
                "answerUser: $answerUser, " +
                "correct: $correct"
    }


    private fun isAnswerValid(answer: String): Boolean {
        return answer.isNotBlank() && answer.toIntOrNull() != null
    }
}