package com.example.asfoapp.ui.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.asfoapp.Constants
import com.example.asfoapp.R
import com.example.asfoapp.databinding.FragmentRecipesListBinding
import com.example.asfoapp.interfaces.OnItemClickListener

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() =
            _binding
                ?: throw IllegalStateException("binding for RecipesListFragment must not be null")

    private val viewModel: RecipesListViewModel by viewModels()
    private val navAgs: RecipesListFragmentArgs by navArgs()
    private var recipesListAdapter: RecipesListAdapter? = null
    private val glideRequestListener = GlideRequestListener()

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
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
        viewModel.loadRecipes(navAgs.category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(state: RecipesListViewModel.RecipesListState) {
        Glide.with(this)
            .load(state.apiHeaderImageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .listener(glideRequestListener)
            .into(binding.categoryImage)
        binding.apply {
            val title = state.categoryTitle
            if(title.isNotBlank()) { categoryName.text = title }
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

class GlideRequestListener : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        e?.logRootCauses(Constants.LOG_TAG)
        Log.e(Constants.LOG_TAG, "Load of image failed", e)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        return false
    }

}

