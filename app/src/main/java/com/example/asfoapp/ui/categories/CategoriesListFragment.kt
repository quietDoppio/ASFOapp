package com.example.asfoapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asfoapp.data.STUB
import com.example.asfoapp.databinding.FragmentCategoriesListBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("binding for CategoriesListFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesListBinding.inflate(inflater, container, false)
        val view = binding.root
        initRecycler(binding)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun initRecycler(binding: FragmentCategoriesListBinding) {
    val categoriesList = STUB.getCategories()
    val adapter = CategoriesListAdapter(categoriesList)
    binding.rvCategories.adapter = adapter
}