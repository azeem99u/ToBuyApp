package com.azeem.tobuyapp

import com.azeem.tobuyapp.database.entity.ItemEntity

interface ItemEntityInterface {
    fun onBumpPriority(itemEntity: ItemEntity)
    fun onItemSelected(itemEntity: ItemEntity)
}