package com.example.asfoapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.asfoapp.R
import com.example.asfoapp.databinding.FragmentCategoriesListBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.recipes.RecipesListFragment

const val ARG_CATEGORY = "ARG_CATEGORY"

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for CategoriesListFragment must not be null")

    private val viewModel: CategoriesViewModel by viewModels()
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        viewModel.loadCategories()
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
            setOnItemClickListener(
                object : OnItemClickListener {
                    override fun onItemClick(itemId: Int) {
                        openRecipesByCategoryId(itemId)
                    }
                }
            )
        }
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.getCategoryById(categoryId)
        category?.let {
            requireActivity().supportFragmentManager.commit {
                val bundle = bundleOf(ARG_CATEGORY to it)
                setReorderingAllowed(true)
                replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
                addToBackStack("CategoriesListFragment")
            }
        }
    }
}





