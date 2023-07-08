package com.jdlstudios.multiplicationmasterapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcherOwner

class MainActivity : AppCompatActivity(), OnBackPressedDispatcherOwner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}