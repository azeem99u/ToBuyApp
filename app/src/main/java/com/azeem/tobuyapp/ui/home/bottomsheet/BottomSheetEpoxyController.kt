package com.azeem.tobuyapp.ui.home.bottomsheet

import com.airbnb.epoxy.EpoxyController
import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.ViewBindingKotlinModel
import com.azeem.tobuyapp.arch.ToBuyViewModel
import com.azeem.tobuyapp.databinding.ModelSortOrderItemBinding

class BottomSheetEpoxyController(
    private val sortOptions: Array<ToBuyViewModel.HomeViewState.Sort>,
    private val selectedCallback: (ToBuyViewModel.HomeViewState.Sort) -> Unit

) : EpoxyController() {
    override fun buildModels() {
        sortOptions.forEach {
            SortOrderItemEpoxyModel(it, selectedCallback).id(it.displayName).addTo(this)
        }
    }

    data class SortOrderItemEpoxyModel(
        val sort: ToBuyViewModel.HomeViewState.Sort,
        val selectedCallback: (ToBuyViewModel.HomeViewState.Sort) -> Unit
    ) : ViewBindingKotlinModel<ModelSortOrderItemBinding>(R.layout.model_sort_order_item) {
        override fun ModelSortOrderItemBinding.bind() {
            textView.text = sort.displayName
            root.setOnClickListener {
                selectedCallback(sort)
            }
        }
    }
}