package com.example.asfoapp.ui.recipes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asfoapp.data.Recipe
import com.example.asfoapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() =
        _binding ?: throw IllegalStateException("binding for RecipeFragment must not be null")

    private var recipe: Recipe? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        recipe = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requireArguments().getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            requireArguments().getParcelable(ARG_RECIPE) as? Recipe
        }

        setContentView()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setContentView(){
        recipe?.let {
            binding.testText.text = recipe.toString()
        }
    }
}
