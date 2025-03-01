package com.example.asfoapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.asfoapp.R
import com.example.asfoapp.data.STUB
import com.example.asfoapp.databinding.FragmentCategoriesListBinding
import com.example.asfoapp.interfaces.OnItemClickListener
import com.example.asfoapp.ui.recipes.RecipesListFragment

const val ARG_CATEGORY = "ARG_CATEGORY"

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for CategoriesListFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesListBinding.inflate(inflater, container, false)
        val view = binding.root
        initRecycler(binding)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(binding: FragmentCategoriesListBinding) {
        val categoriesList = STUB.getCategories()
        val adapter = CategoriesListAdapter(categoriesList)
        adapter.setOnItemClickListener(
            object : OnItemClickListener {
                override fun onItemClick(itemId: Int) {
                    openRecipesByCategoryId(itemId)
                }
            }
        )
        binding.rvCategories.adapter = adapter
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories().find { it.id == categoryId }
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





