package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentConfigurationExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ConfigurationExercisesViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ConfigurationExercisesViewModelFactory

class ConfigurationExercisesFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationExercisesBinding
    private var stateSwitch = 1
    private var quantityExercises: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationExercisesBinding.inflate(inflater)

        //--------------------------------- Para el VIEWMODEL --------------------------------------------------------------
        val application = requireNotNull(this.activity).applicationContext

        val configurationViewModel: ConfigurationExercisesViewModel by viewModels {
            ConfigurationExercisesViewModelFactory((application as MultiplicationApplication).sessionRepository)
        }
        //-------------------------------------------------------------------------------------------------------------------

        getValorSlider()

        binding.switchNumberExercises.isChecked = true
        binding.switchNumberExercises.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onSlider()
                stateSwitch = 1
            } else {
                onEditText()
                stateSwitch = 0
            }
        }

        binding.selectedExerciseAmount.setOnClickListener {
            onEditText()
            stateSwitch = 0
        }

        binding.selectedExerciseAmountEditText.setOnClickListener {

            Toast.makeText(requireContext(), "hiciste click", Toast.LENGTH_SHORT).show()
            if (binding.selectedExerciseAmountEditText.text.toString().isNotEmpty()) {
                quantityExercises =
                    binding.selectedExerciseAmountEditText.text.toString().toInt()
                binding.startButton.isEnabled = true
            } else {
                binding.selectedExerciseAmountEditText.setError("Ingresa un valor")
            }
        }

        binding.startButton.setOnClickListener {
            getQuantityExercises()
            if (quantityExercises != 0) {
                configurationViewModel.sessionForAdd(
                    getSelectedDifficulty(),
                    quantityExercises
                )
                configurationViewModel.insertSession()

                if (quantityExercises > 0) {
                    it.findNavController()
                        .navigate(R.id.action_configurationExercisesFragment_to_exercisesFragment)
                } else {
                    Snackbar.make(
                        binding.root,
                        "La cantidad de ejercicios debe ser mayor a cero",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                Snackbar.make(
                    binding.root,
                    "La cantidad de ejercicios debe ser mayor a cero",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun getValorSlider() {
        binding.sliderQuantity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                binding.startButton.isEnabled = true
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })
        binding.sliderQuantity.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            if (value > 0) {
                binding.selectedExerciseAmount.text = value.toInt().toString()
            } else {
                binding.startButton.isEnabled = false
            }
        }
    }

    private fun onEditText() {
        binding.selectedExerciseAmountEditText.isVisible = true
        binding.selectedExerciseAmount.isVisible = false
        binding.sliderQuantity.isEnabled = false
        binding.switchNumberExercises.isChecked = false
        binding.startButton.isEnabled = false
    }

    private fun onSlider() {
        binding.selectedExerciseAmountEditText.isVisible = false
        binding.selectedExerciseAmount.isVisible = true
        binding.sliderQuantity.isEnabled = true
        binding.startButton.isEnabled = true
    }

    private fun getQuantityExercises(): Int {
        if (stateSwitch == 1) {
            quantityExercises = binding.selectedExerciseAmount.text.toString().toFloat().toInt()
            Log.i("asd", "quantityExercises! 1 :--- $quantityExercises")
        } else {
            if (binding.selectedExerciseAmountEditText.text.toString().isNotEmpty()) {
                quantityExercises =
                    binding.selectedExerciseAmountEditText.text.toString().toInt()

            } else {
                binding.selectedExerciseAmountEditText.error = "Ingresa un valor"
                binding.startButton.isEnabled = true
            }
            Log.i("asd", "quantityExercises! 0 :--- $quantityExercises")
        }
        return quantityExercises
    }

    private fun getSelectedDifficulty(): Difficulty {
        return binding.difficultyGroup.checkedRadioButtonId.let { id ->
            binding.root.findViewById<RadioButton>(id)?.tag?.toString()?.let { tag ->
                Difficulty.getDifficultyFromString(tag)
            } ?: Difficulty.EASY
        }
    }
}

