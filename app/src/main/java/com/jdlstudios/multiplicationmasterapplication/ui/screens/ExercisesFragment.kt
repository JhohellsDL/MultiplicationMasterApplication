package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.data.models.Exercise
import com.jdlstudios.multiplicationmasterapplication.databinding.CardViewAlert1Binding
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ExercisesViewModelFactory

class ExercisesFragment : Fragment() {

    private lateinit var binding: FragmentExercisesBinding
    private lateinit var bindingCard: CardViewAlert1Binding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mostrarCardAlert()
            // Personaliza el comportamiento del botón "Atrás" aquí
            // Puedes realizar acciones como mostrar un diálogo de confirmación, cerrar el fragmento o realizar otras operaciones.
            // Para cerrar el fragmento, puedes utilizar el método popBackStack():
            // requireActivity().supportFragmentManager.popBackStack()

            // Si deseas mantener el comportamiento predeterminado del botón "Atrás" y solo realizar acciones adicionales, puedes llamar al método super.onBackPressed():
            // super.onBackPressed()
        }

    }

    private var mInterstitialAd: InterstitialAd? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercisesBinding.inflate(inflater)

        MobileAds.initialize(requireContext()) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView3.loadAd(adRequest)

        var adRequest2 = AdRequest.Builder().build()

        // Test: ca-app-pub-3940256099942544/1033173712
        // Prod: ca-app-pub-8897050281816485/6389162456
        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/1033173712",
            adRequest2,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })

        // Test: ca-app-pub-3940256099942544/5224354917
        // Prod: ca-app-pub-8897050281816485/5129035187
        binding.buttonIdea.isVisible = false
        val adRequestReward = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/5224354917",
            adRequestReward,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    binding.buttonIdea.isVisible = true
                    rewardedAd = ad
                    val options = ServerSideVerificationOptions.Builder()
                        .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                        .build()
                    rewardedAd!!.setServerSideVerificationOptions(options)
                }
            })

        binding.buttonIdea.setOnClickListener {

            rewardedAd?.let { ad ->
                ad.show(requireContext() as Activity) { rewardItem ->
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    getHelpAfterVideo()
                }
            }

        }

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
            binding.answerEdittext.text.clear()
            val answer = currentExercise!!.answer
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
            exercisesViewModel.exerciseForAdd2(exerciseForAdd)
            exercisesViewModel.insertExercise()

            if (numberExercise == newListExercises.size - 1) {
                exercisesViewModel.sessionForUpdate(
                    correctAnswers = numberCorrects,
                    incorrectAnswers = numberIncorrects,
                    score = getScore(numberTotalExercise, numberCorrects)
                )
                exercisesViewModel.updateSession()
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
                it.findNavController().navigate(
                    ExercisesFragmentDirections.actionExercisesFragmentToFeedbackFragment(
                        currentSession!!.sessionId
                    )
                )
            } else {
                numberExercise++
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

        return binding.root
    }

    private fun getScore(numberTotalExercise: Int, numberCorrects: Int): Int {
        return (100 * numberCorrects) / numberTotalExercise
    }

    private fun isAnswerValid(answer: String): Boolean {
        return answer.isNotBlank() && answer.toIntOrNull() != null
    }

    private fun mostrarCardAlert() {
        bindingCard = CardViewAlert1Binding.inflate(layoutInflater)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(bindingCard.root)
        bindingCard.dialogTitle.text = "ADVERTENCIA "
        bindingCard.dialogMessage.text = "Se perdera tu progreso"
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        bindingCard.buttonPositive.setOnClickListener {
            findNavController().navigate(R.id.action_exercisesFragment_to_configurationExercisesFragment)
            myDialog.cancel()
        }
        bindingCard.buttonNegative.setOnClickListener {
            myDialog.cancel()
        }
    }

    private fun calculateMultiplicationAndDecompose(op1: Int, op2: Int) {
        val result = op1 * op2
        binding.answerEdittext.setText(result.toString())
    }

    private fun getHelpAfterVideo() {
        val op1: Int = currentExercise!!.operand1
        val op2: Int = currentExercise!!.operand2
        calculateMultiplicationAndDecompose(op1, op2)
        binding.buttonIdea.isVisible = false
    }
}