package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.databinding.CardViewAlert1Binding
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentSessionHistoryBinding
import com.jdlstudios.multiplicationmasterapplication.ui.adapters.SessionHistoryAdapter
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.SessionHistoryViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.SessionHistoryViewModelFactory
import com.jdlstudios.multiplicationmasterapplication.utils.Utils

class SessionHistoryFragment : Fragment() {

    private lateinit var bindingCard: CardViewAlert1Binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_sessionHistoryFragment_to_homeFragment)
            // Personaliza el comportamiento del botón "Atrás" aquí
            // Puedes realizar acciones como mostrar un diálogo de confirmación, cerrar el fragmento o realizar otras operaciones.
            // Para cerrar el fragmento, puedes utilizar el método popBackStack():
            // requireActivity().supportFragmentManager.popBackStack()

            // Si deseas mantener el comportamiento predeterminado del botón "Atrás" y solo realizar acciones adicionales, puedes llamar al método super.onBackPressed():
            // super.onBackPressed()
        }

    }

    private lateinit var binding: FragmentSessionHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionHistoryBinding.inflate(inflater)

        val adapter = SessionHistoryAdapter(
            onClickListener = {
                it.let {
                    this.findNavController().navigate(
                        SessionHistoryFragmentDirections.actionSessionHistoryFragmentToFeedbackFragment(
                            it.sessionId
                        )
                    )
                }
            }
        )

        val application = requireNotNull(this.activity).applicationContext
        val sessionHistoryViewModel: SessionHistoryViewModel by viewModels {
            SessionHistoryViewModelFactory(
                (application as MultiplicationApplication).sessionRepository
            )
        }

        sessionHistoryViewModel.listSession.observe(viewLifecycleOwner) {
            adapter.data = it.reversed()
            binding.recyclerView.adapter = adapter
        }

        binding.constraintHome.setOnClickListener {
            it.findNavController().navigate(R.id.action_sessionHistoryFragment_to_homeFragment)
        }

        binding.constraintPractice.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_sessionHistoryFragment_to_configurationExercisesFragment)
        }

        binding.deleteButton.setOnClickListener {
            mostrarCardAlert(sessionHistoryViewModel)
        }

        return binding.root
    }


    private fun mostrarDialogoAlerta(sessionHistoryViewModel: SessionHistoryViewModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Eliminar Datos")
        builder.setMessage("Deseas eliminar todas la sesiones?")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            // Acción a realizar cuando se presiona el botón Aceptar
            sessionHistoryViewModel.deleteAllSessions()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            // Acción a realizar cuando se presiona el botón Cancelar
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun mostrarCardAlert(sessionHistoryViewModel: SessionHistoryViewModel) {
        bindingCard = CardViewAlert1Binding.inflate(layoutInflater)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(bindingCard.root)
        bindingCard.dialogTitle.text = "ELIMINAR DATOS "
        bindingCard.dialogMessage.text = "Deseas eliminar todas la sesiones?"
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        bindingCard.buttonPositive.setOnClickListener {
            sessionHistoryViewModel.deleteAllSessions()
            Utils.SnackbarUtils.showSnackBar(it, "Historial eliminado")
            myDialog.cancel()
        }
        bindingCard.buttonNegative.setOnClickListener {
            myDialog.cancel()
        }
    }


}