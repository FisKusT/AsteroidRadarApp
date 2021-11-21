package com.fiskus.asteroidradarapp.ui.asteroid_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.fiskus.asteroidradarapp.databinding.FragmentAsteroidDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AsteroidDetailsFragment : Fragment() {

    private val viewModel: AsteroidDetailsViewModel by viewModels()
    private lateinit var binding: FragmentAsteroidDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //init binding
        binding = FragmentAsteroidDetailsBinding.inflate(inflater, container, false)

        //set life cycle owner for live data
        binding.lifecycleOwner = viewLifecycleOwner

        //set data binding view model
        binding.viewModel = viewModel

        return binding.root
    }
}