package com.azeem.tobuyapp.ui.customization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyTouchHelper
import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.BaseFragment
import com.azeem.tobuyapp.CustomizationInterface
import com.azeem.tobuyapp.databinding.FragmentCustomizationBinding

class CustomizationFragment : BaseFragment(),CustomizationInterface {
    private var _binding: FragmentCustomizationBinding? = null
    private val binding get() = _binding!!

    private val customizationEpoxyController = CustomizationEpoxyController(this)


    //add category button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.epoxyRecycleView.setController(customizationEpoxyController)
        shareViewModel.categoryEntitiesLiveData.observe(viewLifecycleOwner) { categoriesEntityList ->
            customizationEpoxyController.categories = categoriesEntityList
        }

        EpoxyTouchHelper.initSwiping(binding.epoxyRecycleView)
            .right()
            .withTarget(CustomizationEpoxyController.CategoryEpoxyModel::class.java)
            .andCallbacks(object :
                EpoxyTouchHelper.SwipeCallbacks<CustomizationEpoxyController.CategoryEpoxyModel>(){
                override fun onSwipeCompleted(
                    model: CustomizationEpoxyController.CategoryEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val itemThatWasRemoved = model?.categoryEntity ?:return
                    shareViewModel.deleteCategory(itemThatWasRemoved)
                }

            })

    }

    override fun onCategoryEmptyStateClicked() {
        navigationViewNavGraph(R.id.action_categorizationFragment_to_addCategoryFragment)
    }

    override fun onPrioritySelected(displayText: String) {
        navigationViewNavGraph(CustomizationFragmentDirections.actionCategorizationFragmentToCustomColorFragment(displayText))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}