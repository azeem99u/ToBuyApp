package com.azeem.tobuyapp.ui.add

import android.content.res.ColorStateList
import android.graphics.Typeface
import com.airbnb.epoxy.EpoxyController
import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.ViewBindingKotlinModel
import com.azeem.tobuyapp.addLoadingEpoxyModel
import com.azeem.tobuyapp.arch.ToBuyViewModel
import com.azeem.tobuyapp.databinding.ModelCategoryItemSelectionBinding
import com.azeem.tobuyapp.getAttrColor

class CategoryViewStateEpoxyController(
    private val onCategorySelected:(String) -> Unit
) : EpoxyController() {
    var viewState = ToBuyViewModel.CategoriesViewState()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        if (viewState.isLoading) {
            addLoadingEpoxyModel()
            return
        }
        viewState.itemList.forEach { item ->
            CategoryViewStateItem(item,onCategorySelected).id(item.categoryEntity.id).addTo(this)
        }

    }

    data class CategoryViewStateItem(
        val item: ToBuyViewModel.CategoriesViewState.Item,
        val onCategorySelected: (String) -> Unit

    ) : ViewBindingKotlinModel<ModelCategoryItemSelectionBinding>(R.layout.model_category_item_selection) {
        override fun ModelCategoryItemSelectionBinding.bind() {
            textView.text = item.categoryEntity.name
            root.setOnClickListener {
                onCategorySelected(item.categoryEntity.id)
            }
            val colorRes =
                if (item.isSelected){
                    com.google.android.material.R.attr.colorSecondary
                } else{
                    com.google.android.material.R.attr.colorPrimary
                }
            textView.setTextColor(root.getAttrColor(colorRes))
            textView.typeface = if (item.isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            root.setStrokeColor(ColorStateList.valueOf(colorRes))
        }

    }
}