package com.example.asfoapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asfoapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}