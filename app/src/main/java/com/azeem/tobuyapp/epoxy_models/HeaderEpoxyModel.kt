package com.azeem.tobuyapp.epoxy_models

import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.ViewBindingKotlinModel
import com.azeem.tobuyapp.databinding.ModelHeaderItemBinding

data class HeaderEpoxyModel(val headerText: String) :
    ViewBindingKotlinModel<ModelHeaderItemBinding>(R.layout.model_header_item) {
    override fun ModelHeaderItemBinding.bind() {
        textView.text = headerText
    }
    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}