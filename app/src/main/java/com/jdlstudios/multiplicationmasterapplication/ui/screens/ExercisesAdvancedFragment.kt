package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesAdvancedBinding
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

    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercisesAdvancedBinding.inflate(inflater)

        binding.buttonIdea.isVisible = false
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(TAG, it) }
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    binding.buttonIdea.isVisible = true
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                    val options = ServerSideVerificationOptions.Builder()
                        .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                        .build()
                    rewardedAd!!.setServerSideVerificationOptions(options)
                }
            })

        binding.buttonIdea.setOnClickListener {

            rewardedAd?.let { ad ->
                ad.show(requireContext() as Activity, OnUserEarnedRewardListener { rewardItem ->
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    getHelpAfterVideo()
                })
            } ?: run {
                Log.d(TAG, "The rewarded ad wasn't ready yet.")
            }

        }

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
            val a5 = binding.answerEdittext5.text.toString().toInt()


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
                it.findNavController().navigate(
                    ExercisesAdvancedFragmentDirections.actionExercisesAdvancedFragmentToFeedbackFragment(
                        currentSession!!.sessionId
                    )
                )
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

        binding.answerEdittext.addTextChangedListener(textWatcher)
        binding.answerEdittext2.addTextChangedListener(textWatcher)
        binding.answerEdittext3.addTextChangedListener(textWatcher)
        binding.answerEdittext4.addTextChangedListener(textWatcher)
        binding.answerEdittext5.addTextChangedListener(textWatcher)


        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val editText1Value = binding.answerEdittext.text.toString().trim()
            val editText2Value = binding.answerEdittext2.text.toString().trim()
            val editText3Value = binding.answerEdittext3.text.toString().trim()
            val editText4Value = binding.answerEdittext4.text.toString().trim()
            val editText5Value = binding.answerEdittext5.text.toString().trim()


            binding.submitButton2.isVisible =
                editText1Value.isNotEmpty() && editText2Value.isNotEmpty() && editText3Value.isNotEmpty() && editText4Value.isNotEmpty() && editText5Value.isNotEmpty()
        }
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

    private fun calculateMultiplicationAndDecompose(op1: Int, op2: Int) {
        val unitOp2 = op2 % 10
        val tenOp2 = (op2 / 10) % 10
        val hundredOp2 = op2 / 100

        val result1 = op1 * unitOp2
        val result2 = op1 * tenOp2
        val result3 = op1 * hundredOp2

        displayDecomposedValuesFirst(result1)
        displayDecomposedValuesSecond(result2)
        displayDecomposedValuesThird(result3)
    }

    private fun displayDecomposedValuesThird(number: Int) {
        val unit = number % 10
        val ten = (number / 10) % 10
        val hundred = number / 100

        binding.answerEdittextAux7.setText(unit.toString())
        binding.answerEdittextAux8.setText(ten.toString())
        binding.answerEdittextAux9.setText(hundred.toString())
    }

    private fun displayDecomposedValuesFirst(number: Int) {
        val unit = number % 10
        val ten = (number / 10) % 10
        val hundred = number / 100

        binding.answerEdittextAux1.setText(unit.toString())
        binding.answerEdittextAux2.setText(ten.toString())
        binding.answerEdittextAux3.setText(hundred.toString())
    }

    private fun displayDecomposedValuesSecond(number: Int) {
        val unit = number % 10
        val ten = (number / 10) % 10
        val hundred = number / 100

        binding.answerEdittextAux4.setText(unit.toString())
        binding.answerEdittextAux5.setText(ten.toString())
        binding.answerEdittextAux6.setText(hundred.toString())
    }

    private fun getHelpAfterVideo() {
        val op1: Int = currentExercise!!.operand1
        val op2: Int = currentExercise!!.operand2
        calculateMultiplicationAndDecompose(op1, op2)
        binding.buttonIdea.isVisible = false
    }
}