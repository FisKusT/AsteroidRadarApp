package com.fiskus.asteroidradarapp.ui.asteroid_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fiskus.asteroidradarapp.databinding.ItemAsteroidBinding
import com.fiskus.asteroidradarapp.model.Asteroid

//Asteroids list adapter for Asteroids RV
class AsteroidsListAdapter(private val callback: AsteroidsListAdapterListener): ListAdapter<Asteroid, AsteroidsListAdapter.ViewHolder>(AsteroidsDiffCallback()) {

    //on create view holder- create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    //on bind
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //bind item by position
        holder.bind(getItem(position), callback)
    }

    //view holder class
    class ViewHolder private constructor(private val binding: ItemAsteroidBinding): RecyclerView.ViewHolder(binding.root) {
        //bind method
        fun bind(asteroid: Asteroid, callback: AsteroidsListAdapterListener) {
            //set data binding and execute
            binding.asteroid = asteroid
            binding.callback = callback
            binding.executePendingBindings()
        }


        //view holder constructor
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemAsteroidBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }


    //list data diff replacement
    private class AsteroidsDiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid) = oldItem == newItem
    }

    //callback interface
    interface AsteroidsListAdapterListener {
        //on asteroid click
        fun onClick(asteroid: Asteroid)
    }


}