package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesAdvancedBinding
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesChallengingBinding
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesIntermediateBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModelFactory

class ExercisesAdvancedFragment : Fragment() {

    private lateinit var binding: FragmentExercisesAdvancedBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercisesAdvancedBinding.inflate(inflater)

        val application = requireNotNull(this.activity).applicationContext
        val exercisesViewModel: ExercisesViewModel by viewModels {
            ExercisesViewModelFactory(
                (application as MultiplicationApplication).sessionRepository,
                application.exerciseRepository
            )
        }

        binding.switchHelpAdd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onHelpAdd()
            } else {
                offHelpAdd()
            }
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

                val op1 = currentExercise!!.operand1.toString().toInt() % 10
                val op2 = currentExercise!!.operand2.toString().toInt()

                val editTextList = listOf<EditText>(
                    binding.answerEdittext,
                    binding.answerEdittext2,
                    binding.answerEdittext3,
                    binding.answerEdittext4,
                    binding.answerEdittext5,
                )
                setupSingleDigitInput(editTextList)
            }

        }

        binding.submitButton2.setOnClickListener {

            val a1 = binding.answerEdittext.text.toString().toInt()
            val a2 = binding.answerEdittext2.text.toString().toInt()
            val a3 = binding.answerEdittext3.text.toString().toInt()
            val a4 = binding.answerEdittext4.text.toString().toInt()
            val a5 = binding.answerEdittext4.text.toString().toInt()


            answerUser = (a5 * 10000) + (a4 * 1000) + (a3 * 100) + (a2 * 10) + a1

            cleanAllEditText()

            val answer = currentExercise!!.answer
            Log.i("asd", "Exercise for add: $answer - $answerUser")
            if (answer == answerUser) {
                isCorrect = true
                numberCorrects++
            } else {
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
                exercisesViewModel.sessionForUpdate(
                    numberCorrects,
                    numberIncorrects,
                    getScore(numberTotalExercise, numberCorrects)
                )
                exercisesViewModel.updateSession()
                it.findNavController()
                    .navigate(R.id.action_exercisesAdvancedFragment_to_feedbackFragment)
            } else {
                numberExercise++
                Log.i("asd", "Exercise for add: $numberExercise - ${newListExercises.size}")
            }

            currentExercise = newListExercises[numberExercise]
            binding.quantityExercisesRemainingTextview.text = numberExercise.toString()
            binding.exerciseTextview.text = currentExercise!!.operand1.toString()
            binding.exerciseTextview2.text = currentExercise!!.operand2.toString()

            val op1 = currentExercise!!.operand1.toString().toInt() % 10
            val op2 = currentExercise!!.operand2.toString().toInt()

            val editTextList = listOf<EditText>(
                binding.answerEdittext,
                binding.answerEdittext2,
                binding.answerEdittext3,
                binding.answerEdittext4,
                binding.answerEdittext5,
            )
            setupSingleDigitInput(editTextList)

            binding.answerEdittext.requestFocus()
        }

        binding.answerEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isAnswerValid = isAnswerValid(s.toString())
                binding.submitButton2.isVisible = isAnswerValid
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        return binding.root
    }

    private fun cleanAllEditText() {
        binding.answerEdittextAux1.text.clear()
        binding.answerEdittextAux2.text.clear()
        binding.answerEdittextAux3.text.clear()
        binding.answerEdittextAux4.text.clear()
        binding.answerEdittextAux5.text.clear()
        binding.answerEdittextAux6.text.clear()
        binding.answerEdittextAux7.text.clear()
        binding.answerEdittextAux8.text.clear()
        binding.answerEdittextAux9.text.clear()

        binding.llevaditaEdittext.text.clear()
        binding.llevaditaEdittext2.text.clear()

        binding.answerEdittext.text.clear()
        binding.answerEdittext2.text.clear()
        binding.answerEdittext3.text.clear()
        binding.answerEdittext4.text.clear()
        binding.answerEdittext5.text.clear()

    }

    private fun onHelpAdd() {
        binding.constraintHelpAddition.isVisible = true
        binding.llevaditaEdittext.isVisible = true
        binding.llevaditaEdittext2.isVisible = true
    }

    private fun offHelpAdd() {
        binding.constraintHelpAddition.isVisible = false
        binding.llevaditaEdittext.isVisible = false
        binding.llevaditaEdittext2.isVisible = false
    }
    private fun getScore(numberTotalExercise: Int, numberCorrects: Int): Int {
        var score: Int = 0
        score = (100 * numberCorrects) / numberTotalExercise
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

    private fun setupSingleDigitInput(editTextList: List<EditText>) {
        for (i in editTextList.indices) {
            val editText = editTextList[i]
            if (i < editTextList.size - 1) {
                editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s?.length == 1) {
                            editTextList[i + 1].requestFocus()
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        // No se utiliza
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        // No se utiliza
                    }
                })
            }
        }
    }
}