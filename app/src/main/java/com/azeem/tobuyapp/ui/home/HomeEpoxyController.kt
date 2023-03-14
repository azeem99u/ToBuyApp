package com.azeem.tobuyapp.ui.home

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.azeem.tobuyapp.*
import com.azeem.tobuyapp.arch.ToBuyViewModel
import com.azeem.tobuyapp.database.entity.ItemWithCategoryEntity
import com.azeem.tobuyapp.databinding.ModelItemEntityBinding

class HomeEpoxyController(private val itemEntityInterface: ItemEntityInterface) : EpoxyController() {
    var viewState : ToBuyViewModel.HomeViewState = ToBuyViewModel.HomeViewState(isLoading = true)
        set(value) {
            field = value
            requestModelBuild()
        }


    override fun buildModels() {

        if (viewState.isLoading) {
            addLoadingEpoxyModel()
            return
        }
        if (viewState.dataList.isEmpty()) {
            addEmptyStateEpoxyModel()
            return
        }

        viewState.dataList.forEach { dataItem->
            if (dataItem.isHeader){
                addHeaderModel(dataItem.data as String)
                return@forEach
            }
            val itemWithCategoryEntity = dataItem.data as ItemWithCategoryEntity
            ItemEntityEpoxyModel(itemWithCategoryEntity,itemEntityInterface).id(itemWithCategoryEntity.itemEntity.id)
                .addTo(this)
        }

    }

    data class ItemEntityEpoxyModel(
        val itemEntity: ItemWithCategoryEntity,
        val itemEntityInterface: ItemEntityInterface
    ) : ViewBindingKotlinModel<ModelItemEntityBinding>(R.layout.model_item_entity) {
        override fun ModelItemEntityBinding.bind() {
            titleTextView.text = itemEntity.itemEntity.title
            categoryNameTextView.text = itemEntity.categoryEntity?.name

            if (itemEntity.itemEntity.description == null) {
                descriptionTextView.isGone = true
            } else {
                descriptionTextView.isVisible = true
                descriptionTextView.text = itemEntity.itemEntity.description.toString()
            }

            priorityTextView.setOnClickListener {
                itemEntityInterface.onBumpPriority(itemEntity.itemEntity)
            }

            val color = when (itemEntity.itemEntity.priority) {
                1 -> SharedPrefUtil.getLowPriorityColor()
                2 -> SharedPrefUtil.getMediumPriorityColor()
                3 -> SharedPrefUtil.getHighPriorityColor()
                else -> R.color.gray_800
            }

            priorityTextView.setBackgroundColor(color)
            root.setStrokeColor(ColorStateList.valueOf(color))
            root.setOnClickListener {
                itemEntityInterface.onItemSelected(itemEntity.itemEntity)
            }
        }
    }
}