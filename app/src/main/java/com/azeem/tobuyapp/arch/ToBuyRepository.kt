package com.azeem.tobuyapp.arch

import androidx.lifecycle.LiveData
import com.azeem.tobuyapp.database.AppDatabase
import com.azeem.tobuyapp.database.dao.ItemEntityDao
import com.azeem.tobuyapp.database.entity.CategoryEntity
import com.azeem.tobuyapp.database.entity.ItemEntity
import com.azeem.tobuyapp.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.flow.Flow

class ToBuyRepository(private val appDatabase: AppDatabase) {

    suspend fun insertItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().insert(itemEntity)
    }

    suspend fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().delete(itemEntity)
    }

    fun getAllItems(): Flow<List<ItemEntity>> {
        return appDatabase.itemEntityDao().getAllEntities()
    }

    fun getAllItemWithCategoryEntities(): Flow<List<ItemWithCategoryEntity>> {
        return appDatabase.itemEntityDao().getAllItemWithCategoryEntities()
    }

    suspend fun updateItem(itemEntity: ItemEntity) {
        return appDatabase.itemEntityDao().update(itemEntity)
    }
/////////////////////////
    suspend fun updateCategory(categoryEntity: CategoryEntity) {
        return appDatabase.categoryEntityDao().update(categoryEntity)
    }

    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().insert(categoryEntity)
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().delete(categoryEntity)
    }

    fun getAllCategory(): Flow<List<CategoryEntity>> {
        return appDatabase.categoryEntityDao().getAllCategories()
    }


}