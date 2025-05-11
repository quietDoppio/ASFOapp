package com.example.asfoapp.ui.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.asfoapp.databinding.FragmentRecipesListBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.categories.TAG

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() =
            _binding
                ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private val viewModel: RecipesListViewModel by viewModels()
    private val navAgs: RecipesListFragmentArgs by navArgs()
    private var recipesListAdapter: RecipesListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "RecipeListFragment is created")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.recipesListState.observe(viewLifecycleOwner) { newState ->
            initUi(newState)
        }
        viewModel.loadRecipes(navAgs.category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(state: RecipesListViewModel.RecipesListState) {
        binding.apply {
            state.imageDrawable?.let { categoryImage.setImageDrawable(state.imageDrawable) }
            categoryName.text = navAgs.category.title
            recipesListAdapter?.setData(state.recipes)
        }
    }

    private fun initAdapter() {
        recipesListAdapter = RecipesListAdapter().apply {
            setOnItemClickListener(
                object : OnItemClickListener {
                    override fun onItemClick(itemId: Int) {
                        openRecipeByRecipeId(itemId)
                    }
                }
            )
        }
        binding.rvRecipes.adapter = recipesListAdapter
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = RecipesListFragmentDirections
            .actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

}

