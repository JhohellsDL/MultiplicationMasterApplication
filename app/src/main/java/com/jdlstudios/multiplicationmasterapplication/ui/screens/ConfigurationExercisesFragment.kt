package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
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

        val application = requireNotNull(this.activity).applicationContext
        val configurationViewModel: ConfigurationExercisesViewModel by viewModels {
            ConfigurationExercisesViewModelFactory((application as MultiplicationApplication).sessionRepository)
        }

        getValorSlider()

        binding.switchNumberExercises.isChecked = true
        binding.switchNumberExercises.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onSlider()
                hideKeyboard(binding.selectedExerciseAmountEditText)
                if (binding.selectedExerciseAmountEditText.text.toString().isNotEmpty()) {
                    binding.selectedExerciseAmount.text =
                        binding.selectedExerciseAmountEditText.text.toString()
                } else {
                    binding.selectedExerciseAmount.text = "0"
                }
                stateSwitch = 1
            } else {
                onEditText()
                if (binding.selectedExerciseAmountEditText.text.toString().isNotEmpty()) {
                    binding.selectedExerciseAmount.text =
                        binding.selectedExerciseAmountEditText.text.toString()
                } else {
                    binding.selectedExerciseAmount.text = "0"
                }
                stateSwitch = 0
            }
        }

        binding.selectedExerciseAmount.setOnClickListener {
            onEditText()
            stateSwitch = 0
        }

        binding.selectedExerciseAmountEditText.setOnClickListener {

            if (binding.selectedExerciseAmount.text.toString().toInt() != 0) {
                binding.selectedExerciseAmountEditText.setText(binding.selectedExerciseAmount.text.toString())
            }

            Toast.makeText(requireContext(), "hiciste click", Toast.LENGTH_SHORT).show()
            if (binding.selectedExerciseAmountEditText.text.toString().isNotEmpty()) {
                quantityExercises =
                    binding.selectedExerciseAmountEditText.text.toString().toInt()
                binding.startButton.isEnabled = true
            } else {
                binding.selectedExerciseAmountEditText.error = "Ingresa un valor"
            }
        }
        hideKeyboardOnEnterPress(binding.selectedExerciseAmountEditText)

        binding.startButton.setOnClickListener {
            getQuantityExercises()
            if (quantityExercises != 0) {
                configurationViewModel.sessionForAdd(
                    getSelectedDifficulty(),
                    quantityExercises
                )
                configurationViewModel.insertSession()

                if (quantityExercises > 0) {
                    if (getSelectedDifficulty().ordinal == 0) {
                        Log.i("asd","Diffculty: ${getSelectedDifficulty().ordinal}")
                        it.findNavController()
                            .navigate(R.id.action_configurationExercisesFragment_to_exercisesFragment)
                    } else if (getSelectedDifficulty().ordinal == 1) {
                        Log.i("asd","Diffculty: ${getSelectedDifficulty().ordinal}")
                        it.findNavController()
                            .navigate(R.id.action_configurationExercisesFragment_to_exercisesIntermediateFragment)
                    }else if (getSelectedDifficulty().ordinal == 2) {
                        Log.i("asd","Diffculty: ${getSelectedDifficulty().ordinal}")
                        it.findNavController()
                            .navigate(R.id.action_configurationExercisesFragment_to_exercisesChallengingFragment)
                    }else if (getSelectedDifficulty().ordinal == 3) {
                        Log.i("asd","Diffculty: ${getSelectedDifficulty().ordinal}")
                        it.findNavController()
                            .navigate(R.id.action_configurationExercisesFragment_to_exercisesAdvancedFragment)
                    }

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

    private fun hideKeyboard(editText: EditText) {
        val imm =
            editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun hideKeyboardOnEnterPress(editText: EditText) {
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val imm =
                    editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                if (binding.selectedExerciseAmountEditText.text.toString().isNotEmpty()) {
                    quantityExercises =
                        binding.selectedExerciseAmountEditText.text.toString().toInt()
                    binding.startButton.isEnabled = true
                } else {
                    binding.selectedExerciseAmountEditText.error = "Ingresa un valor"
                    binding.startButton.isEnabled = false
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun getValorSlider() {
        binding.sliderQuantity.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                binding.startButton.isEnabled = true
            }

            override fun onStopTrackingTouch(slider: Slider) {
            }
        })
        binding.sliderQuantity.addOnChangeListener { _, value, _ ->
            if (value > 0) {
                binding.selectedExerciseAmount.text = value.toInt().toString()
            } else {
                binding.selectedExerciseAmount.text = value.toInt().toString()
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
        } else {
            if (binding.selectedExerciseAmountEditText.text.toString().isNotEmpty()) {
                quantityExercises =
                    binding.selectedExerciseAmountEditText.text.toString().toInt()

            } else {
                binding.selectedExerciseAmountEditText.error = "Ingresa un valor"
                binding.startButton.isEnabled = true
            }
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

