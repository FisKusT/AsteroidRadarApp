package com.fiskus.asteroidradarapp.ui.asteroid_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fiskus.asteroidradarapp.databinding.FragmentAsteroidListBinding
import com.fiskus.asteroidradarapp.model.Asteroid
import com.fiskus.asteroidradarapp.utils.InternetStatus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

//enable dagger
@AndroidEntryPoint
class AsteroidListFragment : Fragment() {

    private val viewModel: AsteroidListViewModel by viewModels()
    private lateinit var binding: FragmentAsteroidListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.d("onCreateView")
        //init binding
        binding = FragmentAsteroidListBinding.inflate(inflater, container, false)

        //set life cycle owner for live data
        binding.lifecycleOwner = viewLifecycleOwner

        //set binding view model
        binding.viewmodel = viewModel

        //init RV
        initAsteroidRV()

        //view model observers
        //observe navigation to details page
        viewModel.navigateToDetails.observe(viewLifecycleOwner) { asteroid->
            asteroid?.let {
                //if asteroid is not null navigate to details page
                findNavController().navigate(AsteroidListFragmentDirections.actionAsteroidListFragmentToAsteroidDetailsFragment(asteroid))

                //reset events
                viewModel.resetEvents()
            }
        }

        //observe the asteroid list data
        viewModel.repo.asteroidsList.observe(viewLifecycleOwner) { asteroidsList->
            if(asteroidsList.isEmpty()) {
                Timber.d("load data for the first time")
                //load data for the first time
                viewModel.loadFirstData()
            } else{
                Timber.d("Set Asteroids list")
                //set the filtered list
                viewModel.onAsteroidListFilterClick(viewModel.filteredValue)
            }
        }

        //observe call status
        viewModel.repo.asteroidsStatus.observe(viewLifecycleOwner) { status->
            if (status == InternetStatus.ERROR) {
                viewModel.onInternetFailure(requireContext())
            }
        }

        //chip group click listener
        binding.asteroidListBody.daysFilterCG.forEach { chip->
            chip.setOnClickListener {
                //cal on asteroid list filter with the days to filter value tag
                viewModel.onAsteroidListFilterClick(chip.tag.toString().toInt())
            }

        }

        return binding.root
    }

    //init asteroid RV
    private fun initAsteroidRV() {
        //get RV
        val asteroidsListRV = binding.asteroidListBody.asteroidRV

        //init adapter
        val adapter = AsteroidsListAdapter(object: AsteroidsListAdapter.AsteroidsListAdapterListener {
            override fun onClick(asteroid: Asteroid) {
                //call view model on asteroid click
                viewModel.onAsteroidClick(asteroid)
            }
        })

        //set adapter data change
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
            //call on asteroids list data change
            override fun onChanged() {
                viewModel.onAsteroidListDataChange(asteroidsListRV)
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                viewModel.onAsteroidListDataChange(asteroidsListRV)
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                viewModel.onAsteroidListDataChange(asteroidsListRV)
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                viewModel.onAsteroidListDataChange(asteroidsListRV)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                viewModel.onAsteroidListDataChange(asteroidsListRV)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                viewModel.onAsteroidListDataChange(asteroidsListRV)
            }
        })

        //set adapter to RV
        asteroidsListRV.adapter =adapter
    }
}