package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.practiceButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_configurationExercisesFragment)
        }
        binding.historyButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_sessionHistoryFragment)
        }

        return binding.root
    }

}