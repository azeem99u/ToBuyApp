package com.azeem.tobuyapp

import com.azeem.tobuyapp.database.entity.CategoryEntity
import com.azeem.tobuyapp.database.entity.ItemEntity

interface CustomizationInterface {
        fun onCategoryEmptyStateClicked()
        fun onPrioritySelected(displayText: String)
}