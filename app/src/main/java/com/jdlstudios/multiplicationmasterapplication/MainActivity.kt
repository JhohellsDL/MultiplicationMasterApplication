package com.jdlstudios.multiplicationmasterapplication

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity(), OnBackPressedDispatcherOwner {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        // Se obtiene el controlador de márgenes de ventana
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // Configura el comportamiento de las barras del sistema cuando se ocultan
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Agrega un listener para actualizar el comportamiento del botón de pantalla completa
        // cuando las barras del sistema se ocultan o muestran.
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->

            // Se verifica si las barras del sistema (barra de navegación y barra de estado) están visibles
            val areNavigationBarsVisible =
                windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
            val areStatusBarsVisible = windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())

            // Si alguna de las barras del sistema está visible, se oculta la barra de navegación
            if (areNavigationBarsVisible || areStatusBarsVisible) {
                windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
            }

            // Devuelve los márgenes de ventana después de aplicar los cambios necesarios
            // para que otros componentes de la interfaz también respondan adecuadamente.
            view.onApplyWindowInsets(windowInsets)
        }

        setContentView(R.layout.activity_main)
    }
}