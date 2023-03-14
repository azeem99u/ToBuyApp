package com.azeem.tobuyapp.epoxy_models

import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.ViewBindingKotlinModel
import com.azeem.tobuyapp.databinding.ModelEmptyStateBinding


class EmptyStateEpoxyModel :
    ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state) {
    override fun ModelEmptyStateBinding.bind() {
        //nothing to do
    }

}