package com.azeem.tobuyapp.ui.customization.add_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.azeem.tobuyapp.database.entity.CategoryEntity
import com.azeem.tobuyapp.databinding.FragmentAddCategoryBinding
import com.azeem.tobuyapp.BaseFragment
import java.util.*

class AddCategoryFragment : BaseFragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryNameEditText.apply {
            requestFocus()
            showKeyboard()
        }
        shareViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                navigationUp()
            }
        }

        binding.saveButton.setOnClickListener{
            saveCategoryToDatabase()
        }

    }


    private fun saveCategoryToDatabase() {

        val categoryName = binding.categoryNameEditText.text.toString().trim()
        if (categoryName.isEmpty()) {
            binding.categoryNameTextField.error = "* Required field"
            return
        }
        binding.categoryNameTextField.error = null
        val categoryEntity = CategoryEntity(
            id = UUID.randomUUID().toString(),
            name =  categoryName
        )
        shareViewModel.insertCategory(categoryEntity)
        Toast.makeText(requireContext(),"Category Saved!",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}