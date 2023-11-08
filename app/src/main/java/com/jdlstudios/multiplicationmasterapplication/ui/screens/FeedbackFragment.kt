package com.jdlstudios.multiplicationmasterapplication.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import java.util.Locale

class FeedbackFragment : Fragment() {

    private lateinit var binding: FragmentFeedbackBinding
    private var currentTimeMillis: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(inflater)

        val args = FeedbackFragmentArgs.fromBundle(requireArguments())
        val adapter = FeedbackAdapter()

        val application = requireNotNull(this.activity).applicationContext
        val feedbackViewModel: FeedbackViewModel by viewModels {
            FeedbackViewModelFactory(
                (application as MultiplicationApplication).sessionRepository,
                application.exerciseRepository,
                args.sessionId
            )
        }

        feedbackViewModel.listExercises.observe(viewLifecycleOwner) {
            adapter.data = it
            binding.recyclerView.adapter = adapter
        }

        feedbackViewModel.currentSession.observe(viewLifecycleOwner) {
            setValues(it)
            if (it.sessionId == args.sessionId) {
                feedbackViewModel.getListExercises()
            } else {
                feedbackViewModel.getCurrentSession(args.sessionId)
                feedbackViewModel.getListExercises(args.sessionId)
            }
        }

        binding.restartButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_feedbackFragment_to_sessionHistoryFragment)
        }

        binding.sharedButton.setOnClickListener {
            captureAndSaveImage()
        }
        return binding.root
    }

    private fun setValues(session: SessionEntity) {
        binding.textDifficulty.text =
            Difficulty.getDifficultyFromInt(session.difficulty).toString()
        binding.correctTextview.text = session.correctAnswers.toString()
        binding.incorrectTextview.text = session.incorrectAnswers.toString()
        currentTimeMillis = session.timestamp
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
        binding.timeTextview.text = dateFormat.format(Date(currentTimeMillis))
        binding.scoreTextview.text = String.format("%d / 100 pts", session.score)
        binding.progressBarCorrectIncorrect.progress = session.score
        binding.numberExercisesTextview.text =
            String.format("%d Ejercicios", session.numberOfExercises)
    }

    private fun captureAndSaveImage() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault())
        val message =
            "Esta es mi practica a la fecha: ${dateFormat.format(Date(currentTimeMillis))}"

        val screenshot = captureFragmentScreen(requireParentFragment())
        val imagePath = screenshot?.let {
            saveImage(it, requireContext())
        }
        if (imagePath != null) {
            shareImage(imagePath, message)
        } else {
            Toast.makeText(requireContext(), "Oops!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun captureFragmentScreen(fragment: Fragment): Bitmap? {
        val fragmentView = fragment.view

        if (fragmentView != null && fragmentView.width > 0 && fragmentView.height > 0) {
            try {
                val bitmap =
                    fragmentView.let {
                        Bitmap.createBitmap(
                            it.width,
                            fragmentView.height,
                            Bitmap.Config.ARGB_8888
                        )
                    }

                val canvas = bitmap?.let { Canvas(it) }
                fragmentView.draw(canvas)
                return bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Log.i("asd", "-- no hay captura dentro!")
        }
        return null
    }

    private fun saveImage(image: Bitmap, context: Context): Uri? {
        val imageFileName = "screenshot_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/MyAppScreenshots"
            )
        }

        val resolver = context.contentResolver
        var outputStream: OutputStream? = null
        val imageUri: Uri?
        try {
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            imageUri = resolver.insert(contentUri, contentValues)
            imageUri?.let {
                outputStream = resolver.openOutputStream(it)
                image.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
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

        intent.putExtra(Intent.EXTRA_TITLE, "Compartir imagen")

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(intent, "Compartir imagen"))
    }

}