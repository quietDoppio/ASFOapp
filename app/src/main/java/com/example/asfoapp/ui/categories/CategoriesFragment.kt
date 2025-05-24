package com.example.asfoapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.asfoapp.di.AsfoApplication
import com.example.asfoapp.ui.ViewModelFactory
import com.example.asfoapp.databinding.FragmentCategoriesListBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import kotlinx.coroutines.launch

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for CategoriesListFragment must not be null")
    private var viewModel: CategoriesViewModel? = null
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initAdapter()
        viewModel?.let { vm ->
            vm.categoriesState.observe(viewLifecycleOwner) { newState ->
                initUi(newState)
            }
            vm.toastMessage.observe(viewLifecycleOwner) { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            vm.loadCategories()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi(state: CategoriesViewModel.CategoriesState) {
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

    private fun initViewModel() {
        val context = context?.applicationContext
        context?.let {
            val repository =
                (context as AsfoApplication).container.categoryRepository
            val factory = ViewModelFactory(
                mapOf(CategoriesViewModel::class.java to { CategoriesViewModel(repository) })
            )
            viewModel = ViewModelProvider(this, factory)[CategoriesViewModel::class.java]
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val category = viewModel?.getCategoryById(categoryId)
            val action =
                CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                    category
                )
            findNavController().navigate(action)
        }
    }
}