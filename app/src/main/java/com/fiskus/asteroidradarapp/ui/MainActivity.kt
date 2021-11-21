package com.fiskus.asteroidradarapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fiskus.asteroidradarapp.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}