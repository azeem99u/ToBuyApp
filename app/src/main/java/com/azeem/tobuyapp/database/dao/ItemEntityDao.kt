package com.azeem.tobuyapp.database.dao

import androidx.room.*
import com.azeem.tobuyapp.database.entity.ItemEntity
import com.azeem.tobuyapp.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemEntityDao {

    @Query("select * from item_entity")
    fun getAllEntities():Flow<List<ItemEntity>>

    @Transaction
    @Query("select * from item_entity")
    fun getAllItemWithCategoryEntities():Flow<List<ItemWithCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itemEntity: ItemEntity)

    @Delete
    suspend fun delete(itemEntity: ItemEntity)

    @Update
    suspend fun update(itemEntity: ItemEntity)
}