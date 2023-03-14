package com.azeem.tobuyapp.ui.customization

import android.content.res.ColorStateList
import com.airbnb.epoxy.EpoxyController
import com.azeem.tobuyapp.*
import com.azeem.tobuyapp.database.entity.CategoryEntity
import com.azeem.tobuyapp.databinding.ModelCategoryBinding
import com.azeem.tobuyapp.databinding.ModelEmptyButtonBinding
import com.azeem.tobuyapp.databinding.ModelPriorityColorItemBinding

class CustomizationEpoxyController(
    private val customizationInterface: CustomizationInterface
) : EpoxyController(){

    var categories: List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }
    override fun buildModels() {
        addHeaderModel("Categories")

        categories.forEach {
            CategoryEpoxyModel(it).id(it.id).addTo(this)
        }

        EmptyButtonEpoxyModel("Add Category",customizationInterface)
            .id("add_category")
            .addTo(this)

        addHeaderModel("Priorities")
        val highPriorityColor = SharedPrefUtil.getHighPriorityColor()
        val mediumPriorityColor = SharedPrefUtil.getMediumPriorityColor()
        val lowPriorityColor = SharedPrefUtil.getLowPriorityColor()

        PrioritiesColorItemEpoxyModel("High",highPriorityColor,customizationInterface)
            .id("high_priority")
            .addTo(this)
        PrioritiesColorItemEpoxyModel("Medium",mediumPriorityColor,customizationInterface)
            .id("medium_priority")
            .addTo(this)
        PrioritiesColorItemEpoxyModel("Low",lowPriorityColor,customizationInterface)
            .id("low_priority")
            .addTo(this)

    }


    data class CategoryEpoxyModel(
        val categoryEntity: CategoryEntity
    ) : ViewBindingKotlinModel<ModelCategoryBinding>(R.layout.model_category) {
        override fun ModelCategoryBinding.bind() {
            textView.text = categoryEntity.name
        }
    }

    data class EmptyButtonEpoxyModel(
        val buttonText: String,
        val customizationInterface: CustomizationInterface
    ) : ViewBindingKotlinModel<ModelEmptyButtonBinding>(R.layout.model_empty_button) {
        override fun ModelEmptyButtonBinding.bind() {
            button.text = buttonText
            button.setOnClickListener { customizationInterface.onCategoryEmptyStateClicked() }
        }
        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }

    data class PrioritiesColorItemEpoxyModel(
        val displayText:String,
        val displayColor:Int,
        val customizationInterface: CustomizationInterface
    ):ViewBindingKotlinModel<ModelPriorityColorItemBinding>(R.layout.model_priority_color_item){
        override fun ModelPriorityColorItemBinding.bind() {
            textView.text = displayText
            imageView.setBackgroundColor(displayColor)
            root.setStrokeColor(ColorStateList.valueOf(displayColor))
            imageView.setOnClickListener {
                customizationInterface.onPrioritySelected(displayText)
            }

        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }

    }






}