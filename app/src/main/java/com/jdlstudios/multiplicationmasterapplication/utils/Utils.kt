package com.jdlstudios.multiplicationmasterapplication.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.jdlstudios.multiplicationmasterapplication.R

class Utils {
    object SnackbarUtils {
        fun showSnackBar(rootView: View, message: String) {
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(rootView.context, R.color.accent4))
                .show()
        }
    }
}