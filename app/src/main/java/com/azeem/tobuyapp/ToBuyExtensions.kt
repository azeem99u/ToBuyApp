package com.azeem.tobuyapp

import android.view.View
import androidx.annotation.ColorInt
import com.airbnb.epoxy.EpoxyController
import com.azeem.tobuyapp.epoxy_models.EmptyStateEpoxyModel
import com.azeem.tobuyapp.epoxy_models.HeaderEpoxyModel
import com.azeem.tobuyapp.epoxy_models.LoadingEpoxyModel
import com.google.android.material.color.MaterialColors

fun EpoxyController.addHeaderModel(headerText:String){
    HeaderEpoxyModel(headerText).id(headerText).addTo(this)

}
fun EpoxyController.addEmptyStateEpoxyModel(){
    EmptyStateEpoxyModel().id("empty_sate").addTo(this)
}
fun EpoxyController.addLoadingEpoxyModel(){
    LoadingEpoxyModel().id("loading_state").addTo(this)
}
fun getHeaderTextForPriority(currentPriority: Int): String {
    return when (currentPriority) {
        1 -> "Low"
        2 -> "Medium"
        else -> "High"
    }
}

@ColorInt
fun View.getAttrColor(attrResId:Int):Int{
    return MaterialColors.getColor(this,attrResId)
}

