package com.example.asfoapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.databinding.FragmentFavoritesBinding
import com.example.asfoapp.di.AsfoApplication
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.ViewModelFactory
import com.example.asfoapp.ui.recipes.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for FavoritesFragment must not be null")
    private var viewModel: FavoritesViewModel? = null
    private var recipesListAdapter: RecipesListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewModel()
        viewModel?.let { vm ->
            vm.favoritesState.observe(viewLifecycleOwner) { newState ->
                initUi(newState)
            }
            vm.toastMessage.observe(viewLifecycleOwner) { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            vm.loadRecipes()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(state: FavoritesViewModel.FavoritesState) {
        handleRecyclerVisibleStatus(state.favoritesRecipes)
        recipesListAdapter?.setData(state.favoritesRecipes)
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

    private fun initViewModel() {
        context?.applicationContext?.let {
            val repository = (it as AsfoApplication).container.recipesRepository
            val factory = ViewModelFactory(
                mapOf(FavoritesViewModel::class.java to { FavoritesViewModel(repository) })
            )
            viewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    private fun handleRecyclerVisibleStatus(recipes: List<Recipe>) {
        binding.apply {
            tvEmptyListStub.visibility = View.VISIBLE.takeIf { recipes.isEmpty() } ?: View.GONE
            rvRecipes.visibility = View.GONE.takeIf { recipes.isEmpty() } ?: View.VISIBLE
        }
    }
}