package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentConfigurationExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty

class ConfigurationExercisesFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationExercisesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflar el diseño del fragmento y asignarlo a la variable binding.
        binding = FragmentConfigurationExercisesBinding.inflate(inflater)

        // Agregar un escuchador al SeekBar de cantidad de ejercicios para actualizar la etiqueta.
        binding.seekbarQuantity.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.selectedExerciseAmount.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Agregar un escuchador al botón de inicio para obtener los datos seleccionados y navegar al fragmento de ejercicios.
        binding.startButton.setOnClickListener {
            // Obtener la dificultad seleccionada del grupo de botones de opción.
            val selectedDifficulty = binding.difficultyGroup.checkedRadioButtonId.let { id ->
                binding.root.findViewById<RadioButton>(id)?.tag?.toString()?.let { tag ->
                    Difficulty.getDifficultyFromString(tag)
                } ?: Difficulty.EASY
            }
            // Obtener la cantidad de ejercicios seleccionada del SeekBar.
            val quantityExercises = binding.seekbarQuantity.progress
            // Convertir la dificultad a un entero para pasarlo como argumento.
            val difficultyInt: Int = selectedDifficulty.ordinal

            // Verificar si la cantidad de ejercicios seleccionada es mayor que cero.
            if (quantityExercises > 0) {
                // Mostrar un mensaje de tostada con los datos seleccionados y navegar al fragmento de ejercicios.
                Toast.makeText(context, "-> $selectedDifficulty - $quantityExercises <-", Toast.LENGTH_SHORT).show()
                findNavController().navigate(ConfigurationExercisesFragmentDirections.actionConfigurationExercisesFragmentToExercisesFragment(quantityExercises, difficultyInt))
            } else {
                // Mostrar un mensaje de tostada si la cantidad de ejercicios seleccionada es cero.
                Toast.makeText(context, "La cantidad de ejercicios debe ser mayor a cero", Toast.LENGTH_SHORT).show()
            }
        }

        // Devolver la vista raíz del fragmento.
        return binding.root
    }
}