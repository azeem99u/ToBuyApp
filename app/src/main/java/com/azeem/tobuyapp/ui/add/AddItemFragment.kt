package com.azeem.tobuyapp.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.azeem.tobuyapp.Event
import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.database.entity.CategoryEntity
import com.azeem.tobuyapp.database.entity.ItemEntity
import com.azeem.tobuyapp.databinding.FragmentAddItemBinding
import com.azeem.tobuyapp.BaseFragment
import java.lang.Exception
import java.util.*

class AddItemFragment : BaseFragment() {
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    private var isInEditMode: Boolean = false

    private val safeArgs: AddItemFragmentArgs by navArgs()
    private val selectedItemEntity: ItemEntity? by lazy {
        shareViewModel.itemWithCategoryEntityLiveData.value?.find {
            it.itemEntity.id == safeArgs.selectedItemEntity
        }?.itemEntity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener {
            saveItemEntityToDatabase()
        }
        binding.quantitySeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentText = binding.titleEditText.text.toString().trim()
                if (currentText.isEmpty()) {
                    return
                }
                val startIndex = currentText.indexOf("[") - 1
                val newText = if (startIndex > 0) {
                    "${currentText.substring(0, startIndex)} [$progress]"
                } else {
                    "$currentText [$progress]"
                }
                val sanitizedText = newText.replace(" [1]", "")
                binding.titleEditText.setText(sanitizedText)
                binding.titleEditText.setSelection(sanitizedText.length)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //
            }

        })
        shareViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                if (isInEditMode) {
                    navigationUp()
                    return@observe
                }

                Toast.makeText(requireActivity(), "Item Saved!", Toast.LENGTH_SHORT).show()
                binding.titleEditText.text = null
                binding.titleEditText.apply {
                    requestFocus()
                    showKeyboard()
                }

                binding.descriptionEditText.text = null
                binding.radioGroup.check(R.id.radioButtonLow)
            }

            binding.titleEditText.apply {
                showKeyboard()
                requestFocus()
            }
        }

        selectedItemEntity?.let { itemEntity ->
            isInEditMode = true

            binding.titleEditText.setText(itemEntity.title)
            binding.titleEditText.setSelection(itemEntity.title.length)
            binding.descriptionEditText.setText(itemEntity.description)
            when (itemEntity.priority) {
                1 -> binding.radioGroup.check(R.id.radioButtonLow)
                2 -> binding.radioGroup.check(R.id.radioButtonMedium)
                else -> binding.radioGroup.check(R.id.radioButtonHigh)
            }
            binding.saveButton.text = "Update"
            mainActivity.supportActionBar?.title = "Update Item"

            if (itemEntity.title.contains("[")) {
                val startIndex = itemEntity.title.indexOf("[") + 1
                val endIndex = itemEntity.title.indexOf("]")
                try {
                    val progress = itemEntity.title.substring(startIndex, endIndex).toInt()
                    binding.quantitySeekBar.progress = progress
                } catch (e: Exception) {
                    //
                }
            }
        }
        val categoryViewStateEpoxyController = CategoryViewStateEpoxyController{ categoryId ->
            shareViewModel.onCategorySelected(categoryId)

        }
        binding.categoriesEpoxyRecycleView.setController(categoryViewStateEpoxyController)

        shareViewModel.onCategorySelected(selectedItemEntity?.categoryId ?: CategoryEntity.DEFAULT_CATEGORY_ID,true)
        shareViewModel.categoriesViewStateLiveData.observe(viewLifecycleOwner){ viewState ->
            categoryViewStateEpoxyController.viewState = viewState
        }
    }


    private fun saveItemEntityToDatabase() {
        val itemTitle = binding.titleEditText.text.toString().trim()
        if (itemTitle.isEmpty()) {
            binding.titleTextField.error = "* Required field"
            return
        }
        binding.titleTextField.error = null

        var itemDescription: String? = binding.descriptionEditText.text.toString().trim()
        if (itemDescription?.isEmpty() == true) {
            itemDescription = null
        }

        val itemPriority = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioButtonLow -> 1
            R.id.radioButtonMedium -> 2
            R.id.radioButtonHigh -> 3
            else -> 0
        }

        val itemCategoryId = shareViewModel.categoriesViewStateLiveData.value?.getSelectedCategoryId() ?: return

        val itemEntity = ItemEntity(
            id = UUID.randomUUID().toString(),
            title = itemTitle,
            description = itemDescription,
            priority = itemPriority,
            createdAt = System.currentTimeMillis(),
            categoryId = itemCategoryId
        )


        if (isInEditMode) {
            val itemEntity = selectedItemEntity!!.copy(
                title = itemTitle,
                description = itemDescription,
                priority = itemPriority,
                categoryId = itemCategoryId
            )
            shareViewModel.updateItem(itemEntity)
            shareViewModel.transactionCompleteLiveData.postValue(Event(true))
            return
        }
        shareViewModel.insertItem(itemEntity)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}