package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentConfigurationExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ConfigurationExercisesViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.ConfigurationExercisesViewModelFactory

class ConfigurationExercisesFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationExercisesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationExercisesBinding.inflate(inflater)

        //--------------------------------- Para el VIEWMODEL --------------------------------------------------------------
        val application = requireNotNull(this.activity).applicationContext

        val configurationViewModel: ConfigurationExercisesViewModel by viewModels {
            ConfigurationExercisesViewModelFactory((application as MultiplicationApplication).repository)
        }
        //-------------------------------------------------------------------------------------------------------------------

        getProgressOfSeekBar()

        binding.startButton.setOnClickListener {
            configurationViewModel.sessionForAdd(getSelectedDifficulty(), getQuantityExercises())
            configurationViewModel.insertSession()

            if (getQuantityExercises() > 0) {
                navigateToExercisesFragment(90)
            } else {
                Toast.makeText(
                    context,
                    "La cantidad de ejercicios debe ser mayor a cero",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        return binding.root
    }

    private fun getProgressOfSeekBar() {
        binding.seekbarQuantity.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.selectedExerciseAmount.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun navigateToExercisesFragment(sessionId: Long) {
        findNavController().navigate(
            ConfigurationExercisesFragmentDirections.actionConfigurationExercisesFragmentToExercisesFragment(
                sessionId
            )
        )
    }

    private fun getQuantityExercises() = binding.seekbarQuantity.progress
    private fun getSelectedDifficulty(): Difficulty {
        return binding.difficultyGroup.checkedRadioButtonId.let { id ->
            binding.root.findViewById<RadioButton>(id)?.tag?.toString()?.let { tag ->
                Difficulty.getDifficultyFromString(tag)
            } ?: Difficulty.EASY
        }
    }
}