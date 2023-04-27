package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentConfigurationExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.domain.models.ExerciseItem

class ConfigurationExercisesFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationExercisesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationExercisesBinding.inflate(inflater)

        binding.seekbarQuantity.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.selectedExerciseAmount.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.startButton.setOnClickListener {
            val selectedDifficulty = binding.difficultyGroup.checkedRadioButtonId.let { id ->
                binding.root.findViewById<RadioButton>(id)?.tag?.toString()?.let { tag ->
                    Difficulty.getDifficultyFromString(tag)
                } ?: Difficulty.EASY
            }
            val difficultyInt: Int = selectedDifficulty.ordinal
            val quantityExercises = binding.seekbarQuantity.progress

            Toast.makeText(context, "-> $selectedDifficulty - $quantityExercises <-", Toast.LENGTH_SHORT).show()
            findNavController().navigate(ConfigurationExercisesFragmentDirections.actionConfigurationExercisesFragmentToExercisesFragment(quantityExercises, difficultyInt))
        }

        return binding.root
    }
}