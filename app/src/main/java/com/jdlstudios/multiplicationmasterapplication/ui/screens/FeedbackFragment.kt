package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jdlstudios.multiplicationmasterapplication.MultiplicationApplication
import com.jdlstudios.multiplicationmasterapplication.R
import com.jdlstudios.multiplicationmasterapplication.data.local.models.SessionEntity
import com.jdlstudios.multiplicationmasterapplication.databinding.FragmentFeedbackBinding
import com.jdlstudios.multiplicationmasterapplication.domain.models.Difficulty
import com.jdlstudios.multiplicationmasterapplication.ui.adapters.FeedbackAdapter
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.FeedbackViewModel
import com.jdlstudios.multiplicationmasterapplication.ui.viewmodels.FeedbackViewModelFactory
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date

class FeedbackFragment : Fragment() {

    private lateinit var binding: FragmentFeedbackBinding
    private var currentTimeMillis: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(inflater)

        val adapter: FeedbackAdapter = FeedbackAdapter()

        val application = requireNotNull(this.activity).applicationContext
        val feedbackViewModel: FeedbackViewModel by viewModels {
            FeedbackViewModelFactory(
                (application as MultiplicationApplication).sessionRepository,
                application.exerciseRepository
            )
        }

        feedbackViewModel.listExercises.observe(viewLifecycleOwner) {
            adapter.data = it
            binding.recyclerView.adapter = adapter
        }

        feedbackViewModel.currentSession.observe(viewLifecycleOwner) {
            binding.textDifficulty.text = Difficulty.getDifficultyFromInt(it.difficulty).toString()
            binding.correctTextview.text = it.correctAnswers.toString()
            binding.incorrectTextview.text = it.incorrectAnswers.toString()
            currentTimeMillis = it.timestamp
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss")
            binding.timeTextview.text = dateFormat.format(Date(currentTimeMillis))
            binding.scoreTextview.text = String.format("%d / 100 pts", it.score)
            binding.progressBarCorrectIncorrect.progress = it.score
            binding.numberExercisesTextview.text =
                String.format("%d Ejercicios", it.numberOfExercises)
            feedbackViewModel.getListExercises(it.sessionId)
        }

        binding.restartButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_feedbackFragment_to_sessionHistoryFragment)
        }

        binding.sharedButton.setOnClickListener {
            captureAndSaveImage()
        }
        return binding.root
    }

    private fun captureAndSaveImage() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss")
        val message =
            "Esta es mi practica a la fecha: ${dateFormat.format(Date(currentTimeMillis))}"

        val screenshot = captureFragmentScreen(requireParentFragment())
        val imagePath = screenshot?.let {
            saveImage(it)
        }
        if (imagePath != null) {
            shareImage(imagePath, message)
        } else {
            Toast.makeText(requireContext(), "Oops!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun captureFragmentScreen(fragment: Fragment): Bitmap? {

        val view = fragment.view

        val bitmap =
            view?.let { Bitmap.createBitmap(it.width, view.height - 180, Bitmap.Config.ARGB_8888) }

        val canvas = bitmap?.let { Canvas(it) }
        if (view != null) {
            view.draw(canvas)
        } else {
            Log.i("asd", "-- no hay captura dentro!")
        }

        return bitmap
    }

    private fun saveImage(image: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "screenshot.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/MyAppScreenshots"
            )
        }

        val resolver = requireContext().contentResolver
        var outputStream: OutputStream? = null
        val imageUri: Uri?
        try {
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            imageUri = resolver.insert(contentUri, contentValues)
            imageUri?.let {
                outputStream = resolver.openOutputStream(it)
                image.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream?.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return imageUri
    }

    private fun shareImage(imagePath: Uri, message: String) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, imagePath)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, "Compartir imagen"))
    }

    fun sessionToString(session: SessionEntity): String {
        return """
        |Session Details:
        |Difficulty: ${session.sessionId}
        |Difficulty: ${session.difficulty}
        |Number of Exercises: ${session.numberOfExercises}
        |Correct Answers: ${session.correctAnswers}
        |Incorrect Answers: ${session.incorrectAnswers}
        |Score: ${session.score}
        |Timestamp: ${session.timestamp}
    """.trimMargin()
    }
}