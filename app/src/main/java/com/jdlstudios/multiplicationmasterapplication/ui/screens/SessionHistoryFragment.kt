package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentExercisesBinding
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentSessionHistoryBinding
import com.jdlstudios.multiplicationmasterapplication.ui.adapters.FeedbackAdapter
import com.jdlstudios.multiplicationmasterapplication.ui.adapters.SessionHistoryAdapter
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.FeedbackViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.FeedbackViewModelFactory
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.SessionHistoryViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.SessionHistoryViewModelFactory

class SessionHistoryFragment : Fragment() {

    private lateinit var binding: FragmentSessionHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionHistoryBinding.inflate(inflater)

        val adapter = SessionHistoryAdapter()

        val application = requireNotNull(this.activity).applicationContext
        val sessionHistoryViewModel: SessionHistoryViewModel by viewModels {
            SessionHistoryViewModelFactory(
                (application as MultiplicationApplication).sessionRepository
            )
        }

        sessionHistoryViewModel.listSession.observe(viewLifecycleOwner){
            adapter.data = it
            Log.i("asd","lista actual de ejercicios - $it")
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

}