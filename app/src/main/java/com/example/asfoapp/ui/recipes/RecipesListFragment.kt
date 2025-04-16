package com.example.asfoapp.ui.recipes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.asfoapp.R
import com.example.asfoapp.model.Category
import com.example.asfoapp.databinding.FragmentRecipesListBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.categories.ARG_CATEGORY

const val ARG_RECIPE_ID = "ARG_RECIPE_ID"

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() =
            _binding
                ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private val viewModel: RecipesListViewModel by viewModels()
    private var category: Category? = null
    private var recipesListAdapter: RecipesListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARG_CATEGORY, Category::class.java)
        } else {
            requireArguments().getParcelable(ARG_CATEGORY) as? Category
        }
        initAdapter()
        viewModel.recipesListState.observe(viewLifecycleOwner) { newState ->
            initUi(newState)
        }
        category?.let { viewModel.loadRecipes(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(state: RecipesListViewModel.RecipesListState) {
        binding.apply {
            state.imageDrawable?.let { categoryImage.setImageDrawable(state.imageDrawable) }
            categoryName.text = category?.title
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
        val bundle = bundleOf(ARG_RECIPE_ID to recipeId)
        findNavController().navigate(R.id.action_recipesListFragment_to_recipeFragment, bundle)
    }

}

