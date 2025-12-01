package com.example.asfoapp.presentation.screens.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.asfoapp.databinding.FragmentCategoriesListBinding
import com.example.asfoapp.presentation.interfaces.OnItemClickListener
import com.example.asfoapp.presentation.model.toUiModel
import com.example.asfoapp.domain.models.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {
    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for CategoriesListFragment must not be null")

    private val viewModel: CategoriesViewModel by viewModels()
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.categoriesState.observe(viewLifecycleOwner) { newState ->
            initUi(newState)
        }
        viewModel.toastMessage.observe(viewLifecycleOwner) { messageRes ->
            messageRes?.let {
                Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loadCategories()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(state: CategoriesState) {
        categoriesAdapter?.setData(state.categoriesList)
    }

    private fun initAdapter() {
        categoriesAdapter = CategoriesAdapter().apply {
            setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(itemId: Int) {
                    openRecipesByCategoryId(itemId)
                }
            })
        }
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val category: Category? = viewModel.getCategoryById(categoryId)
            val action =
                CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                    category?.toUiModel()
                )
            findNavController().navigate(action)
        }
    }
}
